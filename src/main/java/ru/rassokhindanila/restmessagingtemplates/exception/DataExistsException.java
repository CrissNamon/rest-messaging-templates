package ru.rassokhindanila.restmessagingtemplates.exception;


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
