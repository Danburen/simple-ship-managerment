package org.waterwood.shipmanagerment.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.waterwood.shipmanagerment.infrastructure.validation.validator.StrongPasswordValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = StrongPasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "密码格式错误，必须包含大小写字符以及数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
