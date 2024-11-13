package com.javaweb.mapper;

import com.javaweb.pojo.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("SELECT * FROM cart WHERE userId = #{userId} AND prizeId = #{prizeId}")
    Cart selectCartByUserIdAndPrizeId(Integer userId, Integer prizeId);

    @Insert("INSERT INTO cart(userId, prizeId, quantity) VALUES (#{userId}, #{prizeId}, #{quantity})")
    void insertCart(Cart cart);

    @Update("UPDATE cart SET quantity = #{quantity} WHERE id = #{id}")
    void updateCart(Cart cart);

    @Select("SELECT c.*, p.name, p.cost, p.imgSrc FROM cart c join prizes p on c.prizeId = p.id WHERE userId = #{userId}")
    List<Cart> selectCartListByUserId(Integer userId);

    @Delete("DELETE FROM cart WHERE id = #{id}")
    void deleteCart(Integer id);
}
