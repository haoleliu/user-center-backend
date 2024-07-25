package com.haole.usercenter.model.domain.request;

import lombok.Data;


/**
 * 用户注册请求体
 *
 * @author liu haole
 */

@Data
public class UserRegisterRequest{

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
