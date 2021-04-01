package ru.rassokhindanila.restmessagingtemplates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassokhindanila.restmessagingtemplates.dto.SavedTemplateDto;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

@Mapper(componentModel = "spring")
public interface SavedTemplateDtoMapper {
    SavedTemplateDtoMapper INSTANCE = Mappers.getMapper(SavedTemplateDtoMapper.class);
    SavedTemplate toSavedTemplate(SavedTemplateDto savedTemplateDto);
}
