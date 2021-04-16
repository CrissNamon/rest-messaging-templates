package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.dto.ValidationResult;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;
import ru.rassokhindanila.restmessagingtemplates.repository.SavedTemplateRepository;
import ru.rassokhindanila.restmessagingtemplates.runnable.TemplateScheduleTask;
import ru.rassokhindanila.restmessagingtemplates.service.SavedTemplateService;

@Service
public class SavedTemplateServiceImpl implements SavedTemplateService {

    @Autowired
    private SavedTemplateRepository savedTemplateRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private TemplateScheduleTask templateScheduleTask;

    @Override
    public void save(TemplateDataDto templateDataDto) {
        ValidationResult validationResult = new ValidationResult(templateDataDto);
        if(validationResult.isViolated())
        {
            return;
        }
        if(templateDataDto.getMinutes() > 0) {
            SavedTemplate savedTemplate = new SavedTemplate();
            savedTemplate.setData(templateDataDto.getVariables());
            savedTemplate.setMinutes(templateDataDto.getMinutes());
            savedTemplate.setTemplateId(templateDataDto.getTemplateId());
            SavedTemplate saved = savedTemplateRepository.saveAndFlush(savedTemplate);
            scheduleTemplate(saved);
        }
    }

    @Override
    public void save(TemplateDataDto templateDataDto,
                     VoidParamFunctional<? super Throwable> onError,
                     VoidFunctional onSuccess) {
        ValidationResult validationResult = new ValidationResult(templateDataDto);
        if(validationResult.isViolated())
        {
            onError.action(
                    new NullPointerException(
                            validationResult.toString()
                    )
            );
            return;
        }
        save(templateDataDto);
        onSuccess.action();
    }

    @Override
    public SavedTemplate find(Long id) {
        return savedTemplateRepository.findById(id).orElse(null);
    }

    private void scheduleTemplate(SavedTemplate savedTemplate)
    {
        if(savedTemplate.getMinutes() > 0) {
            templateScheduleTask.setSavedTemplateId(savedTemplate.getId());
            taskScheduler.scheduleWithFixedDelay(
                    templateScheduleTask,
                    savedTemplate.getMinutes() * 60000
            );
        }
    }
}
