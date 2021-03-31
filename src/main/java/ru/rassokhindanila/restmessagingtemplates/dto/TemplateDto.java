package ru.rassokhindanila.restmessagingtemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {

    @NotEmpty(message = "Template id required")
    @Size(max = 64, message = "Template id max size = 64")
    private String templateId;

    @NotEmpty(message = "Template required")
    private String template;

    @NotEmpty(message = "Template recipients required")
    private Set<@NotEmpty String> recipients;
}
