package com.miaoshaproject.service.Model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class UserModel {




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartId) {
        this.thirdPartyId = thirdPartId;
    }

    public String getEncriptPassword() {
        return encriptPassword;
    }

    public void setEncriptPassword(String encriptPassword) {
        this.encriptPassword = encriptPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "性别不能不填")
    private Byte gender;

    @NotNull(message = "年龄不能不填")
    @Min(value = 0,message = "年龄不能小于0岁")
    @Max(value = 150,message = "年龄不能大于150岁")
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telphone;
    private String registerMode;
    private String thirdPartyId;

    @NotBlank(message = "密码不能为空")
    private String encriptPassword;

}
