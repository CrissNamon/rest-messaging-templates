package ru.rassokhindanila.restmessagingtemplates.exception;

/**
 * Represents exception which raised on WebClient error
 */
public class SenderException extends Exception{

    public SenderException()
    {
        super();
    }

    public SenderException(String message)
    {
        super(message);
    }

}
