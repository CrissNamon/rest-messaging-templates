package ru.rassokhindanila.restmessagingtemplates.service;

import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

public interface TemplateService {

   void save(TemplateDto templateDto,
           VoidParamFunctional<? super Exception> onError,
           VoidParamFunctional<Template> onSuccess,
           VoidParamFunctional<Template> onDuplicate);
   Template find(String id);

}
