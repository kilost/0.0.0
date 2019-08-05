package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusienessError;
import com.miaoshaproject.service.Model.UserModel;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.Validatorimpl;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDOMapper userDOMapper;
    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    Validatorimpl validator;

    public UserModel getUserById(Integer id){
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR);
        }
        //采用封装的validator验证入参
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
/*        if(org.apache.commons.lang3.StringUtils.isEmpty(userModel.getName())
                ||userModel.getGender() == null
                ||userModel.getAge() == null
                || org.apache.commons.lang3.StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR);
        }*/
        UserDO userDO = new UserDO();

        //实现UserDO->DataObject的方法
        userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch(DuplicateKeyException dx){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }


        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telphone, String encriptPassword) throws BusinessException {
        //根据phoe获得password，首先需要获取userDO，根据id再获取enpassword
        UserDO userDO = userDOMapper.selectBytelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusienessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //校验password
        if(!org.apache.commons.lang3.StringUtils.equals(encriptPassword,userModel.getEncriptPassword())){
            throw new BusinessException(EmBusienessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncriptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);

        return userDO;
    }

    public UserModel convertFromDataObject(UserDO userDO,UserPasswordDO userPasswordDO){

        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null){
            userModel.setEncriptPassword(userPasswordDO.getEncrptPassword());
        }
            return userModel;
    }
}
