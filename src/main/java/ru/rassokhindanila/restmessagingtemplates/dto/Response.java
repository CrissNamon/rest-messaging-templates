package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents HTTP response object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    /**
     * Response message
     */
    private String message;
}
