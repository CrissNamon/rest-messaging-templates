package ru.rassokhindanila.restmessagingtemplates.exception;

import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents exception which raised if request data is not valid
 */
public class RequestDataException extends Exception{

    /**
     * Data validation results
     */
    private final Set<ConstraintViolation<Object>> wrongData;

    public RequestDataException(String message)
    {
        super(message);
        wrongData = new HashSet<>();
    }

    public RequestDataException(String message, Set<ConstraintViolation<Object>> validation)
    {
        super(message);
        wrongData = validation;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + getValidationResult();
    }

    /**
     * @return Data validation result
     */
    public String getValidationResult()
    {
        return ValidationUtils.getMessages(wrongData);
    }


}
