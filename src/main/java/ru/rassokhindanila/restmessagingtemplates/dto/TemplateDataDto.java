package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Map;

/**
 * Represents template data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDataDto {

    /**
     * Template ID
     */
    @NotNull
    @NotBlank(message = "Template id required")
    @Size(max = 64, message = "Template id max size = 64")
    private String templateId;

    /**
     * Placeholders and their values
     */
    @NotNull
    @NotBlank(message = "Template variables required")
    private Map<String, String> variables;

    /**
     * Schedule
     */
    @NotEmpty
    @NotNull
    @Min(value = 0, message = "Schedule can't be negative")
    private int minutes;
}
