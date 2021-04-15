package ru.rassokhindanila.restmessagingtemplates.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class StringRequest {

    @NotNull(message = "Value can't be null")
    @NotEmpty(message = "Value can't be empty")
    private String value;

    @JsonCreator
    public StringRequest(@JsonProperty("value") String value)
    {
        this.value = value;
    }
}
