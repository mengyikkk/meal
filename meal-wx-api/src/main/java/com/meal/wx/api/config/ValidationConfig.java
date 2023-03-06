package com.meal.wx.api.config;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
@Configuration
public class ValidationConfig {
    private static final Logger log = LoggerFactory.getLogger(ValidationConfig.class);
    @Bean
    public Validator validator(){
        // 如果存在 自定义注解校验器，请 增加入参 AutowireCapableBeanFactory autowireCapableBeanFactory
        // 并增加调用
        // .failFast( true ) .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
        try(ValidatorFactory vf = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(true)
                .buildValidatorFactory()){
            return vf.getValidator();
        }
    }
}
