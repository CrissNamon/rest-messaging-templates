package ru.rassokhindanila.restmessagingtemplates;

public interface Urls {
    String API = "/api";
    String API_VERSION = "v1";
    String API_PATH = API+"/"+API_VERSION;

    interface Template {
        String END_POINT = "/template";
    }
}
