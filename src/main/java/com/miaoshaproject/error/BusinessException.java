package com.miaoshaproject.error;

public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;
    //直接接收错误参数传递完成异常解析
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }
    //自定义errMsg构造参数异常完成传递
    public BusinessException (CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }
   @Override
    public Integer getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg){
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
