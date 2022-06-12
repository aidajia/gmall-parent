package com.atguigu.gmall.user.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.model.to.UserAuthTo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rpc/inner/user")
public class UserRpcRestController {

    @Autowired
    UserAddressService userAddressService;

    @GetMapping("/address/list")
    public Result<List<UserAddress>> getUserAddressList(){
        UserAuthTo auth = AuthUtil.getUserAuth();
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",auth.getUserId());
        //用户地址列表
        List<UserAddress> list = userAddressService.list(wrapper);

        return Result.ok(list);
    }

}
