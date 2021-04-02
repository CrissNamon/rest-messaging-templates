package ru.rassokhindanila.restmessagingtemplates.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Represents some operations on String
 */
public class StringUtils {

    /**
     * Replaces placeholders in template with given values
     * @param template String with placeholders
     * @param variables Map of placeholders and their values
     * @return String with replaced placeholders
     */
    public static String replaceVariables(String template, Map<String, String> variables)
    {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("$"+entry.getKey()+"$", entry.getValue());
        }
        return template;
    }

    /**
     * @param object Object to convert
     * @return JSON string representation of Object
     * @throws JsonProcessingException Raised if Object can't be converted
     */
    public static String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
