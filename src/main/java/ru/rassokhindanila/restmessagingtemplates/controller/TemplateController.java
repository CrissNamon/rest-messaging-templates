package ru.rassokhindanila.restmessagingtemplates.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rassokhindanila.restmessagingtemplates.Urls;
import ru.rassokhindanila.restmessagingtemplates.dto.Response;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(Urls.API_PATH+Urls.Template.END_POINT)
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping("/addTemplate")
    public ResponseEntity<Response> addTemplate(@RequestBody TemplateDto templateDto)
    {
        if(templateDto == null)
        {
            return ResponseEntity.badRequest().build();
        }
        AtomicReference<ResponseEntity<Response>> response = new AtomicReference<>();
        templateService.save(templateDto,
                error -> response.set(
                        ResponseEntity.badRequest().body(new Response(error.getMessage()))
                ),
                template -> response.set(
                        ResponseEntity.ok(
                                new Response("Template saved successfully")
                        )
                ),
                duplicate -> response.set(
                        ResponseEntity.ok(
                                new Response("Template with given id already exists")
                        )
                )
        );
        return response.get();
    }

    @PostMapping("/useTemplate")
    public ResponseEntity<Response> useTemplate(@RequestBody TemplateDataDto templateDataDto)
    {
        if(templateDataDto == null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Response("Used"));
    }

}
