package com.example.demo.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPattern.PatternValidator.class)
public @interface IPattern {

    String message() default "Your input data is not valid (${validatedValue}) - this is our patter: /{param}/";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String param();

    class PatternValidator implements ConstraintValidator<IPattern, String> {
        Pattern param;

        @Override
        public void initialize(IPattern annotation) {
            this.param = Pattern.compile(annotation.param());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return true;
            if (this.param.matcher(value).matches()) return true;
            return false;
        }
    }

}
