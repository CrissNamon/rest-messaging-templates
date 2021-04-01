package ru.rassokhindanila.restmessagingtemplates.runnable;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;
import ru.rassokhindanila.restmessagingtemplates.model.Template;
import ru.rassokhindanila.restmessagingtemplates.service.SavedTemplateService;
import ru.rassokhindanila.restmessagingtemplates.service.TemplateService;

import javax.transaction.Transactional;

@Data
@AllArgsConstructor
@Service
public class TemplateScheduleTask implements Runnable{

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SavedTemplateService savedTemplateService;

    private Logger logger;

    private String templateId;
    private Long savedTemplateId;

    public TemplateScheduleTask()
    {
        logger = LoggerFactory.getLogger(TemplateScheduleTask.class);
    }

    @Override
    @Transactional
    public void run()
    {
        SavedTemplate savedTemplate = savedTemplateService.find(savedTemplateId);
        if(savedTemplate != null)
        {
            Template template = templateService.find(savedTemplate.getTemplateId());
            if(template != null && savedTemplate.getMinutes() > 0)
            {
                try {
                    templateService.sendMessages(template, savedTemplate.getData());
                    logger.info("MESSAGES SENT FROM SCHEDULED TASK");
                }catch (WebClientException e)
                {
                    logger.error("ERROR OCCURRED DURING SCHEDULED TEMPLATE WITH ID: "+savedTemplateId.toString());
                }
            }
        }
    }

}
