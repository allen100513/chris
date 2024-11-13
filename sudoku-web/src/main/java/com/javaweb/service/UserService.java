package com.javaweb.service;

import com.javaweb.pojo.GameScore;
import com.javaweb.pojo.User;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserService {
    String register(User user);

    User login(User user);

    User getUserById(String token);

    void updateUser(String token, User user);

    void saveGameScore(String token, GameScore gameScore);
}
