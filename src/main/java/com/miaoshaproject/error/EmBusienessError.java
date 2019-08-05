package com.miaoshaproject.error;


public enum EmBusienessError implements CommonError {

    //10000开头的错误表示参数相关错误
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    PARAMETER_UNKNOWN_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关的错误定义
    USER_NOT_EXSIT(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"手机号或密码错误"),
    USER_NOT_LOGIN(20003,"用户还未登陆，不能下单"),


    //30000开头为订单信息相关的错误定义
    STOCK_NOT_ENOUGH(30001,"商品库存不足"),
    ;


    private Integer errCode;
    private String errMsg;
    private EmBusienessError(Integer errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    @Override
    public Integer getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
