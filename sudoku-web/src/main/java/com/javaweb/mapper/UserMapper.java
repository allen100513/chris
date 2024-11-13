package com.javaweb.mapper;

import com.javaweb.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into accounts(name, username, password, birthDate, salt) values(#{name}, #{username}, #{password}, #{birthDate}, #{salt})")
    void insert(User user);

    @Select("select * from accounts where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(User user);

    @Select("select * from accounts where id = #{id}")
    User selectById(Integer id);

    void update(User user);

    @Select("select * from accounts where username = #{username}")
    User selectByUsername(String username);
}
