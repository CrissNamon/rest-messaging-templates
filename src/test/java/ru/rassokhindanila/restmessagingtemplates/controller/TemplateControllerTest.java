package ru.rassokhindanila.restmessagingtemplates.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.rassokhindanila.restmessagingtemplates.Urls;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addEmptyTemplate() throws Exception
    {
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addEmptyEndpointsList() throws Exception
    {
        TemplateDto dto = new TemplateDto();
        Set<String> endpoints = new HashSet<>();
        dto.setRecipients(endpoints);
        dto.setTemplate("Test template");
        dto.setTemplateId("TemplateID");
        String json = StringUtils.toJson(dto);
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void useTemplateNullData() throws Exception
    {
        TemplateDataDto templateDataDto = new TemplateDataDto();
        String json = StringUtils.toJson(templateDataDto);
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void useTemplateNoData() throws Exception
    {
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTemplate() throws Exception
    {
        TemplateDto dto = new TemplateDto();
        Set<String> endpoints = new HashSet<>();
        endpoints.add("http://localhost:8080/testendpoint");
        dto.setRecipients(endpoints);
        dto.setTemplate("Test template");
        dto.setTemplateId("TemplateID");
        String json = StringUtils.toJson(dto);
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isOk());
    }

}
