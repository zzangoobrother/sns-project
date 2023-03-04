package com.example.snsproject.fixture;

import com.example.snsproject.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}
