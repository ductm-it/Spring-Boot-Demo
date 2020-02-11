package com.example.demo.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IMin.IntegerValidator.class)
public @interface IMin {

    String message() default "Your input data is not valid (${validatedValue})";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int param();

    class IntegerValidator implements ConstraintValidator<IMin, Integer> {
        Integer param;

        @Override
        public void initialize(IMin annotation) {
            this.param = annotation.param();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            if (value == null)
                return true;
            if (value >= this.param)
                return true;
            return false;
        }
    }

}
