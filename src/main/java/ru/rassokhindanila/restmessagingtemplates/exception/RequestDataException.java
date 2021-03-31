package ru.rassokhindanila.restmessagingtemplates.exception;

import lombok.Data;
import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class RequestDataException extends Exception{

    private final Set<ConstraintViolation<Object>> wrongData;
    private final String message;

    public RequestDataException(String message)
    {
        super(message);
        this.message = message;
        wrongData = new HashSet<>();
    }

    public RequestDataException(String message, Set<ConstraintViolation<Object>> validation)
    {
        super(message);
        this.message = message;
        wrongData = validation;
    }

    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.append(getValidationResult());
        return stringBuilder.toString();
    }

    public String getValidationResult()
    {
        return ValidationUtils.getMessages(wrongData);
    }


}
