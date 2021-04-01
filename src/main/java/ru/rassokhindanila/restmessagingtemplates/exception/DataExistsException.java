package ru.rassokhindanila.restmessagingtemplates.exception;


/**
 * Represents exception which raised if data with such id already exists
 */
public class DataExistsException extends Exception{

    public DataExistsException()
    {
        super();
    }

    public DataExistsException(String message)
    {
        super(message);
    }

}
