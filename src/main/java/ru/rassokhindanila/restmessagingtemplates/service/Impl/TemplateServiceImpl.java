package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.exception.RequestDataException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.mapper.TemplateDtoMapper;
import ru.rassokhindanila.restmessagingtemplates.model.Template;
import ru.rassokhindanila.restmessagingtemplates.repository.TemplateRepository;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;
import ru.rassokhindanila.restmessagingtemplates.util.ValidationUtils;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    private final Logger logger;

    public TemplateServiceImpl()
    {
        logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    }

    @Override
    public void save(TemplateDto templateDto,
                     VoidParamFunctional<? super Exception> onError,
                     VoidParamFunctional<Template> onSuccess,
                     VoidParamFunctional<Template> onDuplicate) {
        Set<ConstraintViolation<Object>> validation =  ValidationUtils.validate(templateDto);
        if(!validation.isEmpty())
        {
            onError.action(
                    new RequestDataException("Wrong request data: ", validation)
            );
            return;
        }
        Template template = TemplateDtoMapper.INSTANCE.dtoToTemplate(templateDto);
        Template isExists = find(
                templateDto.getTemplateId()
        );
        if(isExists != null)
        {
            onDuplicate.action(isExists);
            logger.error("An error occurred while during template saving: Template with given id already exist");
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
}
