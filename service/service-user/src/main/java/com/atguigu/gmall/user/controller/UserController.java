package com.atguigu.gmall.user.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginUserResponseVo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo, HttpServletRequest request){

        //设置Ip登录 (浏览器 -- nginx -- 网关 -- 微服务)
        String ipAddress = IpUtil.getIpAddress(request);
        userInfo.setIpAddr(ipAddress);

        //用户登录
        LoginUserResponseVo responseVo = userInfoService.login(userInfo);
        if(responseVo == null){
            //没有登录成功
            return Result.build("", ResultCodeEnum.LOGIN_ERROR);
        }

        return Result.ok(responseVo);
    }

    /**
     * 用户退出账号
     * @param token
     * @return
     */
    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){

        userInfoService.logout(token);
        return Result.ok();
    }

}
