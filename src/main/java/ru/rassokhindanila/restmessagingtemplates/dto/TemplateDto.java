package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Represents template
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {

    /**
     * Template ID
     */
    @NotNull
    @NotEmpty(message = "Template id required")
    @Size(max = 64, message = "Template id max size = 64")
    private String templateId;

    /**
     * Template string with placeholders
     */
    @NotNull
    @NotEmpty(message = "Template required")
    private String template;

    /**
     * Set of endpoints to send message to
     */
    @NotNull
    @NotEmpty(message = "Template recipients required")
    private Set<Receiver> recipients;
}
