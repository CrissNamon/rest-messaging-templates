package ru.rassokhindanila.restmessagingtemplates.service;

import com.sun.istack.NotNull;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

import java.util.HashMap;

public interface TemplateService {

   void save(TemplateDto templateDto,
           VoidParamFunctional<? super Exception> onError,
           VoidParamFunctional<Template> onSuccess);
   Template find(String id);
   boolean isExists(String id);
   void findAndProceed(String id,
                       VoidParamFunctional<Template> found,
                       VoidFunctional notFound,
                       VoidParamFunctional<? super Exception> onError);
   void sendMessages(@NotNull Template template, HashMap<String, String> variables) throws WebClientException;
}
