package com.ecommerce.product.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringLengthValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringLength {

    String message() default "Product name must be between 2 and 8 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 2; // Minimum string length
    int max() default 8; // Maximum string length
}
