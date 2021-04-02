package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents response from WebClient
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponse {
    /**
     * Response message
     */
    private String message;
}
