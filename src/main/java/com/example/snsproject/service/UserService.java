package com.example.snsproject.service;

import com.example.snsproject.exception.ErrorCode;
import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.model.User;
import com.example.snsproject.model.entity.UserEntity;
import com.example.snsproject.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName).ifPresent(it -> {throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));});

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, password));
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }

        return "";
    }
}
