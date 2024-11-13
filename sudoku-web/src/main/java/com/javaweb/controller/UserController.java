package com.javaweb.controller;

import com.javaweb.pojo.Result;
import com.javaweb.pojo.User;
import com.javaweb.service.UserService;
import com.javaweb.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/regis")
    public Result register(@RequestBody User user) {
        String register = userService.register(user);
        if (register.equals("註冊成功")) {
            log.info("註冊成功");
            return Result.success();
        } else if(register.equals("帳號已存在")){
            log.info("帳號已存在");
            return Result.error("帳號已存在");
        }else{
            log.info("註冊失敗");
            return Result.error("註冊失敗");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        User u = userService.login(user);
        if (u == null) {
            log.info("登入失敗");
            return Result.error("用戶名或密碼錯誤");
        } else {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("name", u.getName());
            claims.put("role", u.getRole());
            String token = JwtUtils.generateToken(claims);
            log.info("登入成功");
            return Result.success(token);
        }
    }

    @GetMapping("/user")
    public Result getUserById(@RequestHeader("token") String token) {
        User user = userService.getUserById(token);
        log.info("獲取用戶信息成功");
        return Result.success(user);
    }

    @PutMapping("/user")
    public Result updateUser(@RequestHeader("token") String token, @RequestBody User user) {
        userService.updateUser(token, user);
        log.info("更新用戶信息成功");
        return Result.success();
    }
}
