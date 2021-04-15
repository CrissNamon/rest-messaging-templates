package ru.rassokhindanila.restmessagingtemplates.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Contains utils for object's fields validation
 */
public class ValidationUtils {

    /**
     * Validates object fields
     * @param object Object for validation
     * @return Set of validation errors
     */
    public static Set<ConstraintViolation<Object>> validate(Object object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        return validator
                .validate(object);
    }

    /**
     * @param validations Set of validation errors
     * @return All validation errors in one string
     */
    public static String getMessages(Set<ConstraintViolation<Object>> validations)
    {
        StringBuilder stringBuilder = new StringBuilder();
        validations.forEach(validation -> {
            stringBuilder.append(validation.getMessage());
            stringBuilder.append(". ");
        });
        return stringBuilder.toString();
    }

    public static boolean isValid(Object object)
    {
        return validate(object).isEmpty();
    }
}
