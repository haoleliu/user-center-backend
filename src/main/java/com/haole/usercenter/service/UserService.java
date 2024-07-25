package com.haole.usercenter.service;

import com.haole.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author vastjoy
* @description 针对表【user】的数据库操作Service
* @createDate 2024-07-16 10:40:47
*/
public interface UserService extends IService<User> {

    /**
     * 用户登录态键
     */

    /**用户注释
     *
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);


    /**
     * 用户登录
     *
     * @param userAccount  账户
     * @param userPassword 密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 用户注册
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

}
