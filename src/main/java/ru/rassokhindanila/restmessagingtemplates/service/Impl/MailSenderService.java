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
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;
import ru.rassokhindanila.restmessagingtemplates.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Service
public class MailSenderService implements SenderService {

    private Logger logger;

    public MailSenderService()
    {
        logger = LoggerFactory.getLogger(MailSenderService.class);
    }

    @Autowired
    private Environment environment;

    private Mono<SenderResponse> sendEmail(String address, String data) throws SenderException
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(
                    environment.getProperty("senderservice.mail.from")
            );
            message.setTo(address);
            message.setSubject("Data");
            message.setText(data);
            getJavaMailSender().send(message);
            SenderResponse senderResponse = new SenderResponse("Email sent");
            return Mono.just(senderResponse);
        } catch (MailException e)
        {
            logger.error("Error occurred");
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
    public Flux<SenderResponse> send(Set<Receiver> receivers, Object data) throws SenderException {
        List<Mono<SenderResponse>> responseList = new ArrayList<>();
        for(Receiver receiver : receivers)
        {
            logger.info("SENDING MESSAGE "+data.toString()+" TO "+receiver.getDestination());
            try {
                responseList.add(
                      send(receiver, data)
                );
            }catch(WebClientException e)
            {
                logger.error("GOT EXCEPTION IN POSTMANY");
            }
        }
        return Flux.merge(responseList);
    }

    @Override
    public boolean canBeSent(Receiver receiver) {
        return false;
    }

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
