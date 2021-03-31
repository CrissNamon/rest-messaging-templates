package ru.rassokhindanila.restmessagingtemplates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

@Mapper(componentModel = "spring")
public interface TemplateDtoMapper {
    TemplateDtoMapper INSTANCE = Mappers.getMapper(TemplateDtoMapper.class);
    Template dtoToTemplate(TemplateDto templateDto);
}
