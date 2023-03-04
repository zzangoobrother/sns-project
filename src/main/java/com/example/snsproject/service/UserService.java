package com.example.snsproject.service;

import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.model.User;
import com.example.snsproject.model.entity.UserEntity;
import com.example.snsproject.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {

        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);

        userEntityRepository.save(new UserEntity());

        return new User();
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException());

        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }

        return "";
    }
}
