package com.example.demo.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.example.demo.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IUsername.UsernameValidator.class)
public @interface IUsername {

    String message() default "The username '${validatedValue}' already exists in database";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UsernameValidator implements ConstraintValidator<IUsername, String> {

        @Autowired
        private UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return false;
            
            if (this.userRepository.countByUsername(value).getData() > 0l) return false;
            return true;
        }

    }

}
