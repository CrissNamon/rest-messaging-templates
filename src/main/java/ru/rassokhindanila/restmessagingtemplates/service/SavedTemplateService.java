package ru.rassokhindanila.restmessagingtemplates.service;

import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;

public interface SavedTemplateService {

    /**
     * Saves template custom data
     * @param templateDataDto Template data DTO
     */
    void save(TemplateDataDto templateDataDto);

    /**
     * Search for template data by id
     * @param id Saved data id
     * @return SavedTemplate if found and null else
     */
    SavedTemplate find(Long id);
}
