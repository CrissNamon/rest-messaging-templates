package ru.rassokhindanila.restmessagingtemplates.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    public static String replaceVariables(String template, HashMap<String, String> variables)
    {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("$"+entry.getKey()+"$", entry.getValue());
        }
        return template;
    }

    public static String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
