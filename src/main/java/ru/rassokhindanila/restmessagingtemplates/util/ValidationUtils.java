package ru.rassokhindanila.restmessagingtemplates.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtils {
    public static Set<ConstraintViolation<Object>> validate(Object object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);
        return constraintViolations;
    }

    public static String getMessages(Set<ConstraintViolation<Object>> validations)
    {
        StringBuilder stringBuilder = new StringBuilder();
        validations.forEach(validation -> {
            stringBuilder.append(validation.getMessage());
            stringBuilder.append(". ");
        });
        return stringBuilder.toString();
    }
}
