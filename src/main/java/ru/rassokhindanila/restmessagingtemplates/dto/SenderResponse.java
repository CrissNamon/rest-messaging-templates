package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents response from SenderService
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponse {
    /**
     * Response message
     */
    private String message;

    private String destination;

    /**
     * Response exception. Null if no exception
     */
    private Exception exception;

    public SenderResponse(String message)
    {
        setMessage(message);
    }

    public SenderResponse(Exception e)
    {
        setException(e);
    }
}
