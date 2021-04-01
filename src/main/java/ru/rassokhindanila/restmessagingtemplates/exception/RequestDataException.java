package ru.rassokhindanila.restmessagingtemplates.exception;

import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class RequestDataException extends Exception{

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

    public String getValidationResult()
    {
        return ValidationUtils.getMessages(wrongData);
    }


}
