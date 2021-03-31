package ru.rassokhindanila.restmessagingtemplates.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDataDto {

    @NotNull
    @NotBlank(message = "Template id required")
    @Size(max = 64, message = "Template id max size = 64")
    private String templateId;

    @NotNull
    @NotBlank(message = "Template variables required")
    private HashMap<String, String> variables;
}
