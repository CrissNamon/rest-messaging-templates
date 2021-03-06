## About
This repository contains spring rest service for sending templated messages to destinations.

## Endpoints
- POST **/api/v1/template/add**
<br>Accepts TemplateDto:
    - templateId: String - Template identifier
    - template: String - String with placeholders like $variable$
    - recipients: Set<Receiver> - Set of receiver objects
        - receiver_type: ReceiverType - Receiver type (MAIL, POST)
        - destination: String - Receiver destination
- POST **/api/v1/template/use**
<br>Accepts TemplateDataDto:
    - templateId: String - Template identifier
    - variables: Map<String, String> - Map of variables and their values
    - minutes: int - Scheduler. If greater than 0, then data will be sent every x minutes
- POST **/api/v1/template/update/{templateId}/**
<br>Accepts StringRequest:
    - value: String - New template message

## Examples
- Add template request:
    - {"templateId": "internshipRequest","template": "Hello $name$! Jetbrains Internship in $TEAM$NAME$ team.","recipients": [{"destination": "test@gmail.ru", "receiver_type":"MAIL"}, {"destination": "http://localhost:8080/testendpoint", "receiver_type":"POST"}]}
- Use template request:
    - {"templateId": "internshipRequest", "variables": {"TEAM$NAME": "Analytics Platform", "name":"Danila"}}
- Update template message request:
    - {"value": "New message"}