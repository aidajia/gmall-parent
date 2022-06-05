package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 购物车
 */
@Controller
public class CartController {

//    public static Map<Thread,Object> threadMap = new HashMap<>();

    @Autowired
    CartFeignClient cartFeignClient;

    /**
     * 购物车列表页
     * @return
     */
    @GetMapping("/cart.html")
    public String cartPage(){

        return "cart/index";

    }

    //http://cart.gmall.com/addCart.html?skuId=42&skuNum=1&sourceType=query
    /**
     * 把商品添加到购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model,
                          HttpServletRequest request){


        String userId = request.getHeader("UserId");
        String userTempId = request.getHeader("UserTempId");


        //远程调用购物车功能
        Result<CartItem> result = cartFeignClient.addSkuToCart(skuId, skuNum);
        if(result.isOk()){
            CartItem item = result.getData();
            model.addAttribute("skuInfo",item);
            model.addAttribute("skuNum",item.getSkuNum());
        }

        return "cart/addCart"; //商品添加成功提示页
    }

}
