package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.Model.OrderModel;

public interface OrderService {
    //实现创建订单的方法
    OrderModel creatOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;
}
