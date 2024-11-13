package com.javaweb.mapper;

import com.javaweb.pojo.Prize;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeMapper {

    @Select("SELECT * FROM prizes")
    List<Prize> selectAll();

    @Delete("DELETE FROM prizes WHERE id = #{id}")
    void deleteById(Integer id);

    @Update("UPDATE prizes SET name = #{name}, cost = #{cost}, imgSrc = #{imgSrc} WHERE id = #{id}")
    void update(Prize prize);

    @Insert("INSERT INTO prizes(name, cost, imgSrc) VALUES(#{name}, #{cost}, #{imgSrc})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Prize prize);
}
