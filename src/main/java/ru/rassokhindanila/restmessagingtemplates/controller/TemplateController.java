package ru.rassokhindanila.restmessagingtemplates.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rassokhindanila.restmessagingtemplates.Urls;
import ru.rassokhindanila.restmessagingtemplates.dto.Response;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.dto.WebClientResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.DataExistsException;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(Urls.API_PATH + Urls.Template.END_POINT)
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    private Logger logger;

    public TemplateController()
    {
        logger = LoggerFactory.getLogger(TemplateController.class);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addTemplate(@RequestBody TemplateDto templateDto) {
        if (templateDto == null) {
            return ResponseEntity.badRequest().build();
        }
        AtomicReference<ResponseEntity<Response>> response = new AtomicReference<>();
        templateService.save(templateDto,
                error -> {
                    ResponseEntity<Response> responseEntity = ResponseEntity.badRequest().build();
                    if(error instanceof DataExistsException)
                    {
                        responseEntity = ResponseEntity
                                .status(HttpStatus.IM_USED)
                                .body(
                                        new Response("Template with given id already exists")
                                );
                    }
                    response.set(responseEntity);
                },
                template -> response.set(
                        ResponseEntity.ok(
                                new Response("Template saved successfully")
                        )
                )
        );
        return response.get();
    }

    @PostMapping("/use")
    public ResponseEntity<Response> useTemplate(@RequestBody TemplateDataDto templateDataDto) {
        if (templateDataDto == null) {
            return ResponseEntity.badRequest().body(new Response("Template id and data required"));
        }
        AtomicReference<ResponseEntity<Response>> response = new AtomicReference<>();
        templateService.findAndProceed(templateDataDto.getTemplateId(),
                template -> {
                    try {
                        templateService.sendMessages(template, templateDataDto.getVariables());
                        response.set(
                                ResponseEntity.ok(
                                        new Response("Sending")
                                )
                        );
                    } catch (WebClientException e) {
                        logger.error(e.getMessage());
                    }
                },
                () -> response.set(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(
                                        new Response("Template with given id not found")
                                )
                ),
                error -> response.set(
                        ResponseEntity.badRequest()
                                .body(
                                        new Response(error.getMessage())
                                )
                )
        );
        return response.get();
    }

    @PostMapping("/test")
    public ResponseEntity<WebClientResponse> testEndPoint(@RequestBody String message)
    {
        return ResponseEntity.ok(new WebClientResponse("GOT MESSAGE: "+message));
    }

}
