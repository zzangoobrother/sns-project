package com.example.snsproject.model;

import com.example.snsproject.model.entity.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updateedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(entity.getId(), entity.getUserName(), entity.getPassword(), entity.getRole(), entity.getRegisteredAt(), entity.getUpdatedAt(), entity.getDeletedAt());
    }
}
