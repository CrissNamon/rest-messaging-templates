## About
This repository contains spring rest service for sending templated messages to destinations.

## Endpoints
- **/api/v1/template/add**
<br>Accepts TemplateDto:
- templateId: String - Template identifier
- template: String - String with placeholders like $variable$
- recipients: Set<Receiver> - Set of receiver objects
    - receiver_type: ReceiverType - Receiver type (MAIL, POST)
    - destination: String - Receiver destination
- **/api/v1/template/use **
<br>
Accepts TemplateDataDto:
- templateId: String - Template identifier
- variables: Map<String, String> - Map of variables and their values
- minutes: int - Scheduler. If greater than 0, then data will be sent every x minutes
