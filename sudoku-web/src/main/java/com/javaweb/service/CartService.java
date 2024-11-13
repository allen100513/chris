package com.javaweb.service;

import com.javaweb.pojo.Cart;

import java.util.List;

public interface CartService{
    void addCart(String token, Cart cart);

    List<Cart> getCartList(String token);

    void updateQuantity(Cart cart);
}
