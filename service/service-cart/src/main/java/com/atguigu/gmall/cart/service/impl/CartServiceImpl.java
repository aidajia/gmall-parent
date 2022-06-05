package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.common.util.JSONS;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.cart.CartItem;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.UserAuthTo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignClient productFeignClient;

    @Override
    public CartItem addSkuToCart(Long skuId, Integer skuNum) {
        //1. 决定使用哪个购物车键
        String cartKey = determinCartKey();

        //2. 保存这个商品
        CartItem cartItem = saveSkuToCart(skuId, skuNum, cartKey);


        return cartItem;
    }

    @Override
    public String determinCartKey() {
        String prefix = RedisConst.CART_KEY_PREFIX;
        UserAuthTo userAuth = AuthUtil.getUserAuth();
        if(userAuth.getUserId() != null){
            return prefix+userAuth.getUserId();
        }else {
            return prefix+userAuth.getUserTempId();
        }
    }

    @Override
    public CartItem saveSkuToCart(Long skuId, Integer num, String cartKey) {
        //1. 绑定一个指定购物车的操作
        BoundHashOperations<String, String, String> cart = redisTemplate.boundHashOps(cartKey);


        //2. 把skuId存到购物车
        //2.1 如果这个没有存过, 就是新增
        Boolean hasKey = cart.hasKey(skuId.toString()); //判断cartKey这个购物车有没有 skuId这个商品
        if(!hasKey){
            //远程调用product服务, 找到这个商品的详细信息
            Result<SkuInfo> skuInfo = productFeignClient.getSkuInfo(skuId);

            //制作一个 CartItem
            CartItem cartItem = converSkuInfoCartItem(skuInfo.getData());
            cartItem.setSkuNum(num);

            //并转为json, 存到redis
            String json = JSONS.toStr(cartItem);
            cart.put(skuId.toString(),json);

            return cartItem;
        }else {
            //如果存过这个,只是数量增加
            String json = cart.get(skuId.toString());
            CartItem item = JSONS.strToObj(json, new TypeReference<CartItem>() {
            });
            //设置新数量
            item.setSkuNum(item.getSkuNum() + num);
            //更新数据
            cart.put(skuId.toString(),JSONS.toStr(item));

            return item;
        }
    }

    @Override
    public List<CartItem> getCartItems() {
        //得到购物车的键
        String cartKey = determinCartKey();
        // 获取这个购物车的商品
        List<CartItem> cartItems = getItems(cartKey);


        return cartItems;
    }

    private List<CartItem> getItems(String cartKey) {
        //1、拿到购物车
        BoundHashOperations<String, String, String> cart = redisTemplate.boundHashOps(cartKey);

        //2、获取所有商品
        List<String> values = cart.values();

        //R apply(T t);
        List<CartItem> collect = values.stream()
                .map((jsonStr) -> {
                    //流式编程，我们无需关注数据遍历逻辑
                    CartItem cartItem = JSONS.strToObj(jsonStr, new TypeReference<CartItem>() {
                    });
                    return cartItem;
                })
                .sorted((o1,o2)-> o2.getUpdateTime().compareTo(o1.getUpdateTime()))
                .collect(Collectors.toList());


        return collect;
    }

    private CartItem converSkuInfoCartItem(SkuInfo data) {
        UserAuthTo userAuth = AuthUtil.getUserAuth();
        CartItem cartItem = new CartItem();

        cartItem.setId(data.getId());

        if (userAuth.getUserId() != null) {
            cartItem.setUserId(userAuth.getUserId().toString());
        } else {
            cartItem.setUserId(userAuth.getUserTempId());
        }

        cartItem.setSkuId(data.getId());
        cartItem.setSkuNum(0);
        cartItem.setSkuDefaultImg(data.getSkuDefaultImg());
        cartItem.setSkuName(data.getSkuName());
        cartItem.setIsChecked(1);
        cartItem.setCreateTime(new Date());
        cartItem.setUpdateTime(new Date());

        //第一次放进购物车的价格
        cartItem.setCartPrice(data.getPrice());
        //实时价格
        cartItem.setSkuPrice(data.getPrice());
        return cartItem;
    }
}
