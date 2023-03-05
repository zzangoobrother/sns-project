package com.example.snsproject.fixture;

import com.example.snsproject.model.entity.PostEntity;
import com.example.snsproject.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Long postId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName(userName);

        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setUser(userEntity);

        return postEntity;
    }
}
