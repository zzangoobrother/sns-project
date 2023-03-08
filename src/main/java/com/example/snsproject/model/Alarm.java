package com.example.snsproject.model;

import com.example.snsproject.model.entity.AlarmEntity;
import com.example.snsproject.model.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(entity.getId(), entity.getAlarmType(), entity.getArgs(), entity.getRegisteredAt(), entity.getUpdatedAt(), entity.getDeletedAt());
    }
}
