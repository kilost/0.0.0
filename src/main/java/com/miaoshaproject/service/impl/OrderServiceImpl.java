package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusienessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.Model.ItemModel;
import com.miaoshaproject.service.Model.OrderModel;
import com.miaoshaproject.service.Model.UserModel;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    OrderDOMapper orderDOMapper;
    @Autowired
    SequenceDOMapper sequenceDOMapper;
    @Override
    public OrderModel creatOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {

        //校验入参
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,"用户不存在");
        }
        if( 0 > amount || amount >99){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,"购买商品数量0——99");
        }
        //下单减库存
        if(!itemService.decreaseStockByOrderAmount(itemId,amount)){
            throw new BusinessException(EmBusienessError.STOCK_NOT_ENOUGH);
        }


        //订单入库
        OrderModel orderModel = new OrderModel();
        //入库之前生成订单号

        orderModel.setOrderId(generatorOrderNo());
        orderModel.setUserId(userModel.getId());
        orderModel.setItemId(itemModel.getId());
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setAmount(amount);
        orderModel.setOrderPrice(itemModel.getPrice().multiply(BigDecimal.valueOf(amount)));

        OrderDO orderDO = convertOrderDOFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //返回前端

        return orderModel;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generatorOrderNo(){
        //1.订单号有16位
        StringBuilder stringBuilder = new StringBuilder();

        //2.前八位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //3.中六位为自增序列
        //获取当前sequenc,并更新sequence值，循环使用
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.selectByPrimaryKey("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);


        //补足6位
        String sequenceStr = String.valueOf(sequence);
        for(int i= 0;i<(6-sequenceStr.length());i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        //4.末尾2位为分库分表位
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
    OrderDO convertOrderDOFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setId(orderModel.getOrderId());
        return orderDO;
    }

}
