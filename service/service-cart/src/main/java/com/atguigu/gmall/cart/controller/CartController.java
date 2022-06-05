package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/cart")
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 获取购物车列表数据
     * @return
     */
    @GetMapping("/cartList")
    public Result cartList(){
        log.info("获取购物车列表");

        List<CartItem> cartItems = cartService.getCartItems();

        return Result.ok(cartItems);
    }

}
