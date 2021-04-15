package ru.rassokhindanila.restmessagingtemplates.service;

import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDataDto;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;

public interface SavedTemplateService {

    /**
     * Saves template custom data
     * @param templateDataDto Template data DTO
     */
    void save(TemplateDataDto templateDataDto);

    /**
     * @param templateDataDto Template data DTO
     * @param onError Called on error
     * @param onSuccess Called if data successfully saved
     */
    void save(TemplateDataDto templateDataDto,
              VoidParamFunctional<? super Throwable> onError,
              VoidFunctional onSuccess);

    /**
     * Search for template data by id
     * @param id Saved data id
     * @return SavedTemplate if found and null else
     */
    SavedTemplate find(Long id);
}
