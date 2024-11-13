package com.javaweb.service.impl;

import com.javaweb.mapper.UserMapper;
import com.javaweb.pojo.GameScore;
import com.javaweb.pojo.User;
import com.javaweb.service.UserService;
import com.javaweb.utils.JwtUtils;
import com.javaweb.utils.PasswordUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public String register(User user) {
        //判斷帳號是否超過15個字或是少於8個字或是姓名超過10個字
        if (user.getUsername().length() > 15 || user.getUsername().length() < 8 ||
            user.getName().length() > 10 || user.getName().isEmpty() ||
            user.getPassword().length() > 15 || user.getPassword().length() < 8) {
            return "註冊失敗";
            //判斷密碼是否有缺少英文字母或數字
        }else if(!user.getPassword().matches(".*[a-zA-Z].*") ||!user.getPassword().matches(".*\\d.*")){
            return "註冊失敗";
            //判斷帳號是否已經存在
        }else if(userMapper.selectByUsername(user.getUsername())!= null){
            return "帳號已存在";
        }else {
            //加密密碼
            String salt = PasswordUtils.getSalt();
            user.setSalt(salt);
            user.setPassword(PasswordUtils.hashPassword(user.getPassword(), salt));
            userMapper.insert(user);
            return "註冊成功";
        }
    }

    @Override
    public User login(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword(), u.getSalt());
            if (hashedPassword.equals(u.getPassword())) {
                return u;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public User getUserById(String token) {
        Claims claims = JwtUtils.parseToken(token);
        Integer id = claims.get("id", Integer.class);
        return userMapper.selectById(id);
    }

    @Override
    public void updateUser(String token, User user) {
        Claims claims = JwtUtils.parseToken(token);
        Integer id = claims.get("id", Integer.class);
        user.setId(id);
        if (user.getPassword() != null) {
            String salt = PasswordUtils.getSalt();
            user.setSalt(salt);
            user.setPassword(PasswordUtils.hashPassword(user.getPassword(), salt));
        }
        userMapper.update(user);
    }

    @Override
    public void saveGameScore(String token, GameScore gameScore) {
        Claims claims = JwtUtils.parseToken(token);
        Integer id = claims.get("id", Integer.class);
        User user = userMapper.selectById(id);
        user.setBonus(user.getBonus() + gameScore.getScore());
        if (user.getRecordSeconds() != null) {
            if(gameScore.getTime() < user.getRecordSeconds()){
                user.setRecordSeconds(gameScore.getTime());
            }
        }else{
            user.setRecordSeconds(gameScore.getTime());
        }
        userMapper.update(user);
    }
}
