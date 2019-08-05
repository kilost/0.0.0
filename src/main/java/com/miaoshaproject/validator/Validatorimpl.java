package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;


@Component
public class Validatorimpl implements InitializingBean {

    private javax.validation.Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet= validator.validate(bean);
        if(constraintViolationSet.size() > 0){
            //有错误
            result.setHasErrors(true);
            //for (ConstraintViolation <Object> objectConstraintViolation : constraintViolationSet) {
            constraintViolationSet.forEach(objectConstraintViolation -> {
                String errMsg = objectConstraintViolation.getMessage();
                String propertyName = objectConstraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errMsg);
            });



        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将Hibernate的validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
