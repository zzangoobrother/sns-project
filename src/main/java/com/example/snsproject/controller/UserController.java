package com.example.snsproject.controller;

import com.example.snsproject.controller.request.UserJoinRequest;
import com.example.snsproject.controller.request.UserLoginRequest;
import com.example.snsproject.controller.response.AlarmResponse;
import com.example.snsproject.controller.response.Response;
import com.example.snsproject.controller.response.UserJoinResponse;
import com.example.snsproject.controller.response.UserLoginResponse;
import com.example.snsproject.model.User;
import com.example.snsproject.service.AlarmService;
import com.example.snsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, @AuthenticationPrincipal User user) {
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal User user) {
        return alarmService.connectAlarm(user.getId());
    }
}
