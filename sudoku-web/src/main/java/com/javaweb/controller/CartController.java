package com.javaweb.controller;

import com.javaweb.pojo.Cart;
import com.javaweb.pojo.Result;
import com.javaweb.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart")
    public Result addCart(@RequestHeader("token") String token, @RequestBody Cart cart){
        cartService.addCart(token, cart);
        return Result.success();
    }

    @GetMapping("/cart")
    public Result getCartList(@RequestHeader("token") String token){
        List<Cart> cartList = cartService.getCartList(token);
        return Result.success(cartList);
    }

    @PutMapping("/cart")
    public Result updateQuantity(@RequestBody Cart cart){
        cartService.updateQuantity(cart);
        log.info("更新購物車成功");
        return Result.success();
    }


}
