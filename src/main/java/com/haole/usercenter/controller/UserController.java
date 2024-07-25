package com.haole.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haole.usercenter.common.BaseResponse;
import com.haole.usercenter.common.ErrorCode;
import com.haole.usercenter.common.ResultUtils;
import com.haole.usercenter.exception.BusinessException;
import com.haole.usercenter.model.domain.User;
import com.haole.usercenter.model.domain.request.UserLoginRequest;
import com.haole.usercenter.model.domain.request.UserRegisterRequest;
import com.haole.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.haole.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.haole.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 *
 * @author liu haole
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            /*return ResultUtils.error(ErrorCode.PARAMS_ERROR);*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userPassword = userRegisterRequest.getUserPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userPassword,userAccount,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();


        if(StringUtils.isAnyBlank(userPassword,userAccount)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }



    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj=request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) userObj;
        if (currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId=currentUser.getId();
        User user=userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }



    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){

        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList=userService.list(queryWrapper);

        //这个逻辑是先把UserList转换为一个数据流，然后遍历其中的每一个元素，将其userPassword设置为空，再将返回的user

        //拼成一个list
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id,HttpServletRequest request){

        if(!isAdmin(request)){
            return null;
        }
        //仅管理员可查询
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(id<=0){
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是否是管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User) userObj;

        return user!=null&&user.getUserRole()==ADMIN_ROLE;

    }



}
