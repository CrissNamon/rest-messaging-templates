package ru.rassokhindanila.restmessagingtemplates.service;

import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.dto.TemplateDto;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidFunctional;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

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
   Template find(String id);

   /**
    * Search for template without returning it
    * @param id Template id to search
    * @return true if template with given id exists
    */
   boolean isExists(String id);

   /**
    * Search for template and proceed some action on it
    * @param id Template id to search
    * @param found Called if template found, returns Template object
    * @param notFound Called if template with given id not found
    * @param onError Called if an error occurred, returns Exception
    */
   void findAndProceed(String id,
                       VoidParamFunctional<Template> found,
                       VoidFunctional notFound,
                       VoidParamFunctional<? super Exception> onError);

   /**
    * Replaces placeholders in template end sends it to endpoints
    * @param template Template object
    * @param variables Map of placeholders and their values
    * @throws SenderException Raised if an error occurred during sending
    */
   void sendMessages(Template template, Map<String, String> variables) throws SenderException;

   /**
    * Replaces placeholders in template end sends it to endpoints
    * @param template Template object
    * @param variables Map of placeholders and their values
    * @param onResponse Called on response for each receiver
    * @throws SenderException Raised if an error occurred during sending
    */
   void sendMessages(Template template, Map<String, String> variables,
                     VoidParamFunctional<SenderResponse> onResponse) throws SenderException;

   /**
    * @param id Template id
    * @param message New template message
    * @param notFound Called if template not found
    * @param onError Called if an error occurred
    * @param onSuccess Called if template was successfully updated
    */
   void updateTemplateMessage(String id, String message,
                              VoidFunctional notFound,
                              VoidParamFunctional<? super Exception> onError,
                              VoidFunctional onSuccess);
}
