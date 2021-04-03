package ru.rassokhindanila.restmessagingtemplates.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import ru.rassokhindanila.restmessagingtemplates.Urls;
import ru.rassokhindanila.restmessagingtemplates.dto.Response;
import ru.rassokhindanila.restmessagingtemplates.dto.StringRequest;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.exception.DataExistsException;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.service.LoggerService;
import ru.rassokhindanila.restmessagingtemplates.service.SavedTemplateService;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller for templates REST
 */
@RestController
@RequestMapping(Urls.API_PATH + Urls.Template.END_POINT)
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SavedTemplateService savedTemplateService;

    @Autowired
    private LoggerService logger;

    /**
     * @param templateDto Template DTO
     * @return HTTP 200 if template successfully added
     *         HTTP 409 if template with given id already exists
     *         HTTP 400 if templateDto has wrong data
     */
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
                                .status(HttpStatus.CONFLICT)
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

    /**
     * @param templateDataDto Template data DTO
     * @return HTTP 200 if template has been successfully used
     *         HTTP 400 if template data DTO is null
     *         HTTP 500 if an error has occurred during sending
     */
    @PostMapping("/use")
    public ResponseEntity<Response> useTemplate(@RequestBody TemplateDataDto templateDataDto) {
        if (templateDataDto == null) {
            return ResponseEntity.badRequest()
                    .body(
                            new Response("Template id and data required")
                    );
        }
        AtomicReference<ResponseEntity<Response>> response = new AtomicReference<>();
        templateService.findAndProceed(templateDataDto.getTemplateId(),
                template -> {
                    try {
                        if(templateDataDto.getMinutes() > 0) {
                            savedTemplateService.save(templateDataDto);
                        }else{
                            templateService.sendMessages(template, templateDataDto.getVariables());
                        }
                        response.set(
                                ResponseEntity.ok(
                                        new Response("Sending")
                                )
                        );
                    } catch (SenderException e) {
                        logger.error(e.getMessage());
                        response.set(
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                        new Response("An Error occurred during sending: "+e.getMessage())
                                )
                        );
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

    @PostMapping("/update/{id}/")
    public ResponseEntity<Response> updateMessage(@PathVariable("id") String id, @RequestBody StringRequest request)
    {
        if(id == null)
        {
            return ResponseEntity.badRequest().body(new Response("Wrong template id"));
        }
        if(request == null)
        {
            return ResponseEntity.badRequest().body(new Response("Request can't be null"));
        }
        if(request.getValue() == null)
        {
            return ResponseEntity.badRequest().body(new Response("Request value can't be null"));
        }
        AtomicReference<ResponseEntity<Response>> response = new AtomicReference<>();
        templateService.updateTemplateMessage(id, request.getValue(),
                () -> response.set(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new Response("Template with given id not found")
                        )
                ),
                error -> response.set(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                new Response("An error occurred")
                        )
                ),
                () -> response.set(
                        ResponseEntity.ok().body(
                                new Response("Updated")
                        )
                )
        );
        return response.get();
    }

    /**
     * Just test endpoint
     * @param message String message
     * @return Always HTTP 200
     */
    @PostMapping("/test")
    public ResponseEntity<Response> testEndPoint(@RequestBody String message)
    {
        return ResponseEntity.ok(new Response(message));
    }

    /**
     * Handles deserialize exception
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="There was an error processing the request body.")
    public void handleMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        logger.error("Can't deserialize request: "+exception.getMessage());
    }

}
