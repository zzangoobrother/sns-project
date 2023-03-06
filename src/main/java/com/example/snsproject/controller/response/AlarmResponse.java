package com.example.snsproject.controller.response;

import com.example.snsproject.model.Alarm;
import com.example.snsproject.model.AlarmArgs;
import com.example.snsproject.model.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(alarm.getId(), alarm.getAlarmType(), alarm.getAlarmArgs(), alarm.getAlarmType().getAlarmText(), alarm.getRegisteredAt(), alarm.getUpdatedAt(), alarm.getDeletedAt());
    }
}
