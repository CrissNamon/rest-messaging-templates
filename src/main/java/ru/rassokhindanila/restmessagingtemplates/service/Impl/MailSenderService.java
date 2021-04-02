package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.service.LoggerService;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MailSenderService implements SenderService {

    @Autowired
    private LoggerService logger;

    @Autowired
    private Environment environment;

    /**
     * @param address Email address
     * @param data Data to send
     * @return SenderResponse of sending result
     * @throws SenderException Raised if an error occurred
     */
    private Mono<SenderResponse> sendEmail(String address, String data) throws SenderException
    {
        String from = environment.getProperty("senderservice.mail.from");
        if(from == null)
        {
            logger.error("application.properties should contain senderservice.mail.from value");
            throw new SenderException("Server error");
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(address);
            message.setSubject("Data");
            message.setText(data);
            getJavaMailSender().send(message);
            SenderResponse senderResponse = new SenderResponse("Email sent");
            senderResponse.setDestination(address);
            return Mono.just(senderResponse);
        } catch (MailException e)
        {
            logger.error("Error occurred while mail sending: "+e.getMessage());
            throw new SenderException("Error occurred while mail sending");
        }
    }

    @Override
    public void send(Receiver receiver, Object data, VoidParamFunctional<SenderResponse> onResponse) throws SenderException {
        try {
            sendEmail(receiver.getDestination(),
                    StringUtils.toJson(data)
            ).subscribe(
                    onResponse::action
            );
        } catch (JsonProcessingException e) {
            throw new SenderException("Serialization exception");
        }
    }

    @Override
    public void send(Set<Receiver> receivers, Object data, VoidParamFunctional<SenderResponse> onResponse) {
        receivers = filterValidReceivers(receivers);
        for(Receiver receiver : receivers)
        {
            try {
                logger.info("SENDING MESSAGE " + data.toString() + " TO " + receiver.getDestination());
                send(receiver, data, onResponse::action);
            }catch(SenderException e)
            {
                logger.error("An error occurred. "+e.getMessage());
                onResponse.action(
                        new SenderResponse(e)
                );
            }
        }
    }

    @Override
    public boolean canBeSent(Receiver receiver) {
        return receiver.getReceiverType() == ReceiverType.MAIL;
    }

    @Override
    public Set<Receiver> filterValidReceivers(Set<Receiver> receivers) {
        return receivers.stream()
                .filter(this::canBeSent)
                .collect(Collectors.toSet());
    }

    /**
     * @return JavaMailSender
     */
    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(
                environment.getProperty("spring.mail.host")
        );
        mailSender.setPort(587);

        mailSender.setUsername(
                environment.getProperty("spring.mail.username")
        );
        mailSender.setPassword(
                environment.getProperty("spring.mail.password")
        );

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}
