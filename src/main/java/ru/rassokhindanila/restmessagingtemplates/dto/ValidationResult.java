package ru.rassokhindanila.restmessagingtemplates.dto;

import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Represents result of object validation
 */
public class ValidationResult {

    private final Set<ConstraintViolation<Object>> violations;

    /**
     * @param object Object for validation
     */
    public ValidationResult(Object object)
    {
        violations = ValidationUtils.validate(object);
    }

    /**
     * @return Set of violations
     */
    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }

    /**
     * @return true if object contains violations
     */
    public boolean isViolated()
    {
        return !violations.isEmpty();
    }

    @Override
    public String toString()
    {
        return ValidationUtils.getMessages(violations);
    }
}
