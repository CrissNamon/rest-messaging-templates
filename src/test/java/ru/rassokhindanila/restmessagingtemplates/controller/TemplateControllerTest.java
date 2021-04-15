package ru.rassokhindanila.restmessagingtemplates.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.rassokhindanila.restmessagingtemplates.Urls;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
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
        Set<Receiver> endpoints = new HashSet<>();
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
        TemplateDataDto templateDataDto = null;
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
        MvcResult mvcResult = mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc
            .perform(asyncDispatch(mvcResult))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void useTemplateNullId() throws Exception
    {
        TemplateDataDto templateDataDto = new TemplateDataDto();
        templateDataDto.setMinutes(0);
        templateDataDto.setTemplateId(null);
        templateDataDto.setVariables(new HashMap<>());
        MvcResult mvcResult = mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                StringUtils.toJson(templateDataDto)
                        )
        )
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTemplate() throws Exception
    {
        TemplateDto dto = new TemplateDto();
        Set<Receiver> endpoints = new HashSet<>();
        endpoints.add(new Receiver(ReceiverType.POST, "http://localhost:8080/testendpoint"));
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

    @Test
    public void addTemplateWrongReceiverType() throws Exception
    {
        String json = "{\n" +
                "\n" +
                "\"templateId\": \"TEST_ID\",\n" +
                "\n" +
                "\"template\": \"TEST TEMPLATE\",\n" +
                "\n" +
                "\"recipients\": [{\"destination\": \"TEST DESTINATION\", \"receiver_type\":\"TEST\"}]\n" +
                "\n" +
                "}";
        mockMvc.perform(
                post(Urls.API_PATH+Urls.Template.END_POINT+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest());
    }

}
