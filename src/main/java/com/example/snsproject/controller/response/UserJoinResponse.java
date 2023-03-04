package com.example.snsproject.controller.response;

import com.example.snsproject.model.User;
import com.example.snsproject.model.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinResponse {

    private Long id;
    private String userName;
    private UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(user.getId(), user.getUserName(), user.getUserRole());
    }
}
