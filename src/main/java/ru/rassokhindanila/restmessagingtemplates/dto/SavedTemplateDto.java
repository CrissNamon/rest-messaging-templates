package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents saved template data DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedTemplateDto {

    /**
     * Template id
     */
    private String templateId;

    /**
     * Template schedule
     */
    private int minutes;

    /**
     * Placeholders with variables
     */
    private Map<String, String> variables;
}
