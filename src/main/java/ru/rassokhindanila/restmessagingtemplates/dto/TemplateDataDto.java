package ru.rassokhindanila.restmessagingtemplates.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;

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
    private HashMap<String, String> variables;

    @Min(value = 0, message = "Schedule can't be negative")
    private int minutes;
}
