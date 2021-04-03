package ru.rassokhindanila.restmessagingtemplates.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StringRequest {

    private String value;

    @JsonCreator
    public StringRequest(@JsonProperty("value") String value)
    {
        this.value = value;
    }
}
