package com.miaoshaproject.response;

public class CommonReturnType {
    //通过通用的Status确定让前端根据服务器反馈的即结果进行处理
    private String Status;
    //若Status为true，则返回需求的Data为Json数据
    //若为“fail”，则返回错误码对应的状态及错误类型
    private Object Data;

    //定义一个通用的创建方法

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
