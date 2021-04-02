package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MailSenderService implements SenderService {

    private Logger logger;

    public MailSenderService()
    {
        logger = LoggerFactory.getLogger(MailSenderService.class);
    }

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
            return Mono.just(senderResponse);
        } catch (MailException e)
        {
            logger.error("Error occurred while mail sending: "+e.getMessage());
            throw new SenderException("Error occurred while mail sending");
        }
    }

    @Override
    public Mono<SenderResponse> send(Receiver receiver, Object data) throws SenderException {
        try {
            return sendEmail(receiver.getDestination(),
                    StringUtils.toJson(data)
            );
        } catch (JsonProcessingException e) {
            throw new SenderException("Serialization exception");
        }
    }

    @Override
    public Flux<SenderResponse> send(Set<Receiver> receivers, Object data) {
        receivers = filterValidReceivers(receivers);
        List<Mono<SenderResponse>> responseList = new ArrayList<>();
        for(Receiver receiver : receivers)
        {
            try {
                logger.info("SENDING MESSAGE " + data.toString() + " TO " + receiver.getDestination());
                responseList.add(
                        send(receiver, data)
                );
            }catch(SenderException e)
            {
                logger.error("An error occurred. "+e.getMessage());
                responseList.add(
                        Mono.just(
                                new SenderResponse(e)
                        )
                );
            }
        }
        return Flux.merge(responseList);
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
