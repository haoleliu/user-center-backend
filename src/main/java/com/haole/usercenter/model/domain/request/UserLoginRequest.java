package com.haole.usercenter.model.domain.request;

import lombok.Data;

/**
 * 用户登录请求体
 *
 * @author liu haole
 */


@Data
public class UserLoginRequest{
    
    private String userAccount;
    private String userPassword;
}
