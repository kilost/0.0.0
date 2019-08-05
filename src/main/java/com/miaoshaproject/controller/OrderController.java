package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusienessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.Model.OrderModel;
import com.miaoshaproject.service.Model.UserModel;
import com.miaoshaproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController extends BaseController{

    @Autowired
    OrderService orderService;
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType creatOrder(@RequestParam(name = "itemId")Integer itemId,
                                       @RequestParam(name = "amount")Integer amount) throws BusinessException {
        //获取用户登陆状态
        Boolean islogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(islogin == null || !islogin.booleanValue()){
            throw new BusinessException(EmBusienessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }
        //获取用户登陆信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.creatOrder(userModel.getId(),itemId,amount);
        return CommonReturnType.create(null);
    }
}
