package com.atguigu.gmall.cart.rpc;


import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rpc/inner/cart")
public class CartRpcController {

    @Autowired
    CartService cartService;

    /**
     * 给购物车中添加一种商品
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/add/{skuId}")
    public Result<CartItem> addSkuToCart(@PathVariable("skuId") Long skuId,
                                         @RequestParam("skuNum") Integer skuNum){



        CartItem cartItem = cartService.addSkuToCart(skuId,skuNum);

        return Result.ok(cartItem);
    }

}
