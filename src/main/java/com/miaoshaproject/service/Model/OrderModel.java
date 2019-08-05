package com.miaoshaproject.service.Model;

import java.math.BigDecimal;

public class OrderModel {
    //订单号，12位组成，前8位表示事件，中间6位是递增序列，最后两位为分裤分表位
    private String orderId;
    //用户号
    private Integer userId;
    //商品id
    private Integer itemId;
    //商品价格
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //下单金额
    private BigDecimal orderPrice;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}