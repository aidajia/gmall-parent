package com.atguigu.gmall.user.service;


import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginUserResponseVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 登录
     * @param userInfo
     * @return
     */
    LoginUserResponseVo login(UserInfo userInfo);

    /**
     * 保存用户认证信息
     * @param userInfo
     * @return
     */
    String saveUserAuthenticationInfo(UserInfo userInfo);

    /**
     * 用户退出账号
     * @param token
     */
    void logout(String token);
}
