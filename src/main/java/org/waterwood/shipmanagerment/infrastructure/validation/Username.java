package org.waterwood.shipmanagerment.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.waterwood.shipmanagerment.infrastructure.validation.validator.UsernameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "用户名格式错误，仅支持6-20位大小写字母和数字组成";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
