package ru.rassokhindanila.restmessagingtemplates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    @Mapping(source = "recipients", target = "recipients", qualifiedByName = "mapToSetReceivers")
    Template toTemplate(TemplateDto templateDto);

    @Named("mapToSetReceivers")
    static Set<Receiver> mapToSetReceivers(Map<ReceiverType, Set<String>> recipients)
    {
        Set<Receiver> receivers = new HashSet<>();
        Receiver receiver;
        for(ReceiverType receiverType : recipients.keySet())
        {
            Set<String> urls = recipients.get(receiverType);
            for(String url : urls)
            {
                receiver = new Receiver();
                receiver.setDestination(url);
                receiver.setReceiverType(receiverType);
                receivers.add(receiver);
            }
        }
        return receivers;
    }
}
