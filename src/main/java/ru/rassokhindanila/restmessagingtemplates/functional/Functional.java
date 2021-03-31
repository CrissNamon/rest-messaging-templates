package ru.rassokhindanila.restmessagingtemplates.functional;

public interface Functional {
    static void empty(){};
    static <V> void consume(V ... variable){}
}
