package ru.rassokhindanila.restmessagingtemplates;

public interface Urls {

    /**
     * API url path
     */
    String API = "/api";
    /**
     * API version
     */
    String API_VERSION = "v1";
    /**
     * Full API url PATH
     */
    String API_PATH = API+"/"+API_VERSION;

    /**
     * Templates endpoint
     */
    interface Template {
        String END_POINT = "/template";
    }
}
