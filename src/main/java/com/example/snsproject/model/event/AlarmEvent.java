package com.example.snsproject.model.event;

import com.example.snsproject.model.AlarmArgs;
import com.example.snsproject.model.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {

    private Long receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs args;
}
