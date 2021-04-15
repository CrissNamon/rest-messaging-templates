package ru.rassokhindanila.restmessagingtemplates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

/**
 * Mapper for template model and DTO
 */
@Mapper(componentModel = "spring")
public interface TemplateDtoMapper {

    TemplateDtoMapper INSTANCE = Mappers.getMapper(TemplateDtoMapper.class);

    /**
     * @param templateDto Template DTO
     * @return Template model
     */
    Template toTemplate(TemplateDto templateDto);

}
