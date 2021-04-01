package ru.rassokhindanila.restmessagingtemplates.exception;

/**
 * Represents exception which raised on WebClient error
 */
public class WebClientException extends Exception{

    public WebClientException()
    {
        super();
    }

    public WebClientException(String message)
    {
        super(message);
    }

}
