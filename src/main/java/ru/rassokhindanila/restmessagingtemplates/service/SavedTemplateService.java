package ru.rassokhindanila.restmessagingtemplates.service;

import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;

public interface SavedTemplateService {

    void save(TemplateDataDto templateDataDto);
    SavedTemplate find(Long id);
}
