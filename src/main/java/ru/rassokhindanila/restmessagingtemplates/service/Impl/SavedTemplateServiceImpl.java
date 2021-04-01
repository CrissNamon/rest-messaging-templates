package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
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
    public void save(@NotNull TemplateDataDto templateDataDto) {
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
    public SavedTemplate find(Long id) {
        return savedTemplateRepository.findById(id).orElse(null);
    }

    public void scheduleTemplate(SavedTemplate savedTemplate)
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
