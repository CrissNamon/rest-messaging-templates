package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents request for WebClient
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderRequest {
    /**
     * Request message
     */
    private String message;
}
