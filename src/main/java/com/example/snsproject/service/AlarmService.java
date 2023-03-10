package com.example.snsproject.service;

import com.example.snsproject.exception.ErrorCode;
import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.model.AlarmArgs;
import com.example.snsproject.model.entity.AlarmEntity;
import com.example.snsproject.model.entity.AlarmType;
import com.example.snsproject.model.entity.UserEntity;
import com.example.snsproject.repository.AlarmEntityRepository;
import com.example.snsproject.repository.EmitterRepository;
import com.example.snsproject.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    private final EmitterRepository emitterRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public void send(AlarmType type, AlarmArgs arg, Long receiverUserId) {
        UserEntity userEntity = userEntityRepository.findById(receiverUserId).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(userEntity, type, arg));

        emitterRepository.get(receiverUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(receiverUserId);
                throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter founded"));
    }

    public SseEmitter connectAlarm(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException exception) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }
}
