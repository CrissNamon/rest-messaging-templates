package ru.rassokhindanila.restmessagingtemplates.service;

import com.sun.istack.NotNull;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Service with operations on templates
 */
public interface TemplateService {

   /**
    * Saving template
    * @param templateDto Template data to save
    * @param onError Called on error
    * @param onSuccess Called on success, returns saved Template
    */
   void save(TemplateDto templateDto,
           VoidParamFunctional<? super Exception> onError,
           VoidParamFunctional<Template> onSuccess);

   /**
    * Search template with given id
    * @param id Template id to search
    * @return Template if found or null if not
    */
   Template find(@NotNull String id);

   /**
    * Search for template without returning it
    * @param id Template id to search
    * @return true if template with given id exists
    */
   boolean isExists(@NotNull String id);

   /**
    * Search for template and proceed some action on it
    * @param id Template id to search
    * @param found Called if template found, returns Template object
    * @param notFound Called if template with given id not found
    * @param onError Called if an error occurred, returns Exception
    */
   void findAndProceed(@NotNull String id,
                       VoidParamFunctional<Template> found,
                       VoidFunctional notFound,
                       VoidParamFunctional<? super Exception> onError);

   /**
    * Replaces placeholders in template end sends it to endpoints
    * @param template Template object
    * @param variables Map of placeholders and their values
    * @throws WebClientException Raised if an error occurred during sending
    */
   void sendMessages(@NotNull Template template, Map<String, String> variables) throws WebClientException;
}
