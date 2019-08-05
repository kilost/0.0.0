package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.Model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone,String encriptPassword) throws BusinessException;
}
