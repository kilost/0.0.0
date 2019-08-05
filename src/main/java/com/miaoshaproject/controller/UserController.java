package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusienessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.Model.UserModel;
import com.miaoshaproject.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
@RequestMapping("user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户登陆接口
    @RequestMapping(value = "/login")
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone) || org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR);
        }
        //登陆服务,校验登录信息是否合法，合法则登陆成功
        UserModel userModel = userService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登陆凭证加入到用户登陆成功的session内
        if(!(userModel == null)){
            this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        }else {
            this.httpServletRequest.getSession().setAttribute("IS_LOGIN",false);
        }

        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }
    //用户注册接口
    @RequestMapping(value = "/register")
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号跟对应的otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(inSessionOtpCode,otpCode)){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }


        //然后再进行注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue()).trim()));
        userModel.setAge(age);
        userModel.setRegisterMode("byphone");
        userModel.setEncriptPassword(EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64En = new BASE64Encoder();
        //加密字符串
        String newstr = base64En.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    @RequestMapping("/getOtp")
    @ResponseBody
    public CommonReturnType getOtpCode(@RequestParam(name = "telphone") String telphone){
        //按一定规则生成Otp验证码
        Random random = new Random();
        int randomInt =(int)random.nextInt(99999)+100000;
        String otpCode = String.valueOf(randomInt);
        //将验证码与手机号关联
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
        //发送验证码
        System.out.println("telphone:"+telphone+"&otpCode:"+otpCode);
        return CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service对象根据用户id获取用户信息返回给前端
        UserModel userModel = userService.getUserById(id);
        if(userModel == null){
            //userModel.setEncriptPassword("111");
            throw new BusinessException(EmBusienessError.USER_NOT_EXSIT);
        }
        UserVo userVo = convertFromMode(userModel);
        return CommonReturnType.create(userVo);

    }
    //Model层对象转换为requstbody接收的对象
    public UserVo convertFromMode(UserModel userModel){
        if (userModel == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }
}
