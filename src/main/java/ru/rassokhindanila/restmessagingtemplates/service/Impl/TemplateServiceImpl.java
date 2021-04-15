package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderRequest;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.exception.DataExistsException;
import ru.rassokhindanila.restmessagingtemplates.exception.RequestDataException;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.Functional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.mapper.TemplateDtoMapper;
import ru.rassokhindanila.restmessagingtemplates.model.Template;
import ru.rassokhindanila.restmessagingtemplates.repository.TemplateRepository;
import ru.rassokhindanila.restmessagingtemplates.service.LoggerService;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;
import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Qualifier("webSenderService")
    @Autowired
    private SenderService webSenderService;

    @Qualifier("mailSenderService")
    @Autowired
    private SenderService mailSenderService;

    @Autowired
    private LoggerService logger;

    @Override
    public void save(TemplateDto templateDto,
                     VoidParamFunctional<? super Exception> onError,
                     VoidParamFunctional<Template> onSuccess) {
        Set<ConstraintViolation<Object>> validation =  ValidationUtils.validate(templateDto);
        if(!validation.isEmpty())
        {
            onError.action(
                    new RequestDataException("Wrong request data: ", validation)
            );
            return;
        }
        Template template = TemplateDtoMapper.INSTANCE.toTemplate(templateDto);
        boolean isExists = isExists(
                templateDto.getTemplateId()
        );
        if(isExists)
        {
            onError.action(
                    new DataExistsException("Template with given id exists")
            );
            return;
        }
        try {
            onSuccess.action(
                    templateRepository.save(template)
            );
        }catch(IllegalArgumentException e) {
            logger.error("An error occurred while during template saving: "+e.getMessage());
            onError.action(e);
        }
    }

    @Override
    public Template find(String id)
    {
        return templateRepository.findById(id).orElse(null);
    }

    @Override
    public void findAndProceed(String id,
                               VoidParamFunctional<Template> found,
                               VoidFunctional notFound,
                               VoidParamFunctional<? super Exception> onError)
    {
        if(id == null)
        {
            onError.action(
                    new NullPointerException("Template id can't be null")
            );
            return;
        }
        Template search = find(id);
        if(search == null)
        {
            notFound.action();
            return;
        }
        found.action(search);
    }

    @Override
    public boolean isExists(String id)
    {
        if(id == null)
        {
            return false;
        }
        return templateRepository.existsById(id);
    }

    @Override
    public void sendMessages(Template template, Map<String, String> variables) throws SenderException
    {
        sendMessages(template, variables, Functional::consume);
    }

    @Override
    public void sendMessages(Template template, Map<String, String> variables,
                             VoidParamFunctional<SenderResponse> onResponse) throws SenderException {
        if(template == null)
        {
            throw new SenderException("Template is null");
        }
        String message = template.getTemplate();
        String updatedMessage = StringUtils.replaceVariables(message, variables);
        SenderRequest request = new SenderRequest(updatedMessage);
        Map<ReceiverType, Set<Receiver>> sortedReceivers = sortReceivers(template.getRecipients());
        for(ReceiverType receiverType : sortedReceivers.keySet())
        {
            switch(receiverType)
            {
                case POST:
                    webSenderService.send(
                            sortedReceivers.get(receiverType),
                            request,
                            response -> {
                                logger.info("WebSenderService response: "+response.getMessage()+" from "
                                + response.getDestination());
                                onResponse.action(response);
                            }
                    );
                    break;
                case MAIL:
                    mailSenderService.send(
                            sortedReceivers.get(receiverType),
                            request,
                            response -> {
                                logger.info("MailSenderService response: "+response.getMessage()+" from "
                                        + response.getDestination());
                                onResponse.action(response);
                            }
                    );
                    break;
            }
        }
    }

    @Override
    public void updateTemplateMessage(String id, String newValue,
                                      VoidFunctional notFound,
                                      VoidParamFunctional<? super Exception> onError,
                                      VoidFunctional onSuccess) {
        findAndProceed(id,
                template -> {
                    template.setTemplate(newValue);
                    save(template);
                    onSuccess.action();
                },
                notFound,
                onError);
    }

    public void save(Template template) {
        templateRepository.save(template);
    }

    private Map<ReceiverType, Set<Receiver>> sortReceivers(Set<Receiver> receivers)
    {
        Map<ReceiverType, Set<Receiver>> sorted = new HashMap<>();
        receivers.forEach(receiver -> {
            if(sorted.containsKey(receiver.getReceiverType()))
            {
                sorted.get(receiver.getReceiverType()).add(receiver);
            }else{
                HashSet<Receiver> receiverHashSet = new HashSet<>();
                receiverHashSet.add(receiver);
                sorted.put(receiver.getReceiverType(), receiverHashSet);
            }
        });
        return sorted;
    }

}
