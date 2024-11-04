package com.javaweb.service.impl;

import com.javaweb.mapper.CartMapper;
import com.javaweb.pojo.Cart;
import com.javaweb.service.CartService;
import com.javaweb.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public void addCart(String token, Cart cart) {
        Claims claims = JwtUtils.parseToken(token);
        Integer userId = claims.get("id", Integer.class);
        Cart c = cartMapper.selectCartByUserIdAndPrizeId(userId, cart.getPrizeId());
        if (c == null) {
            cart.setUserId(userId);
            cartMapper.insertCart(cart);
            System.out.println("添加購物車成功");
        } else {
            c.setQuantity(c.getQuantity() + cart.getQuantity());
            cartMapper.updateCart(c);
            System.out.println("更新購物車成功");
        }
    }

    @Override
    public List<Cart> getCartList(String token) {
        Claims claims = JwtUtils.parseToken(token);
        Integer userId = claims.get("id", Integer.class);
        List<Cart> cartList = cartMapper.selectCartListByUserId(userId);
        return cartList;
    }

    @Override
    public void updateQuantity(Cart cart) {
        if (cart.getQuantity() > 0) {
            cartMapper.updateCart(cart);
        }else {
            cartMapper.deleteCart(cart.getId());
        }
    }

}
