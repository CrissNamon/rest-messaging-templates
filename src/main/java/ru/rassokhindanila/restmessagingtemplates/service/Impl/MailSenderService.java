package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;

import java.util.Set;

@Service
public class MailSenderService implements SenderService {

    private Logger logger;

    public MailSenderService()
    {
        logger = LoggerFactory.getLogger(MailSenderService.class);
    }

    @Override
    public Mono<SenderResponse> send(Receiver receiver, Object data) throws SenderException {
        return null;
    }

    @Override
    public Flux<SenderResponse> send(Set<Receiver> receivers, Object data) throws SenderException {
        logger.info("SENDING MESSAGE "+data+" TO EMAILS");
        return null;
    }

    @Override
    public boolean canBeSent(Receiver receiver) {
        return false;
    }
}
