package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderRequest;
import ru.rassokhindanila.restmessagingtemplates.exception.RequestDataException;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.mapper.TemplateDtoMapper;
import ru.rassokhindanila.restmessagingtemplates.model.Template;
import ru.rassokhindanila.restmessagingtemplates.repository.TemplateRepository;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;
import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Qualifier("webSenderService")
    @Autowired
    private SenderService senderService;

    private final Logger logger;

    public TemplateServiceImpl()
    {
        logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    }

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
                    new Exception("Template with given id exists")
            );
            return;
        }
        try {
            onSuccess.action(
                    templateRepository.save(template)
            );
        }catch(IllegalArgumentException e) {
            onError.action(e);
            logger.error("An error occurred while during template saving: "+e.getMessage());
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
            onError.action(new NullPointerException("Template id can't be null"));
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
        return templateRepository.existsById(id);
    }

    @Override
    public void sendMessages(@NotNull Template template, Map<String, String> variables) throws SenderException
    {
        String message = template.getTemplate();
        String updatedMessage = StringUtils.replaceVariables(message, variables);
        SenderRequest request = new SenderRequest(updatedMessage);
        senderService.send(template.getRecipients(), request)
                .subscribe();
    }

}
