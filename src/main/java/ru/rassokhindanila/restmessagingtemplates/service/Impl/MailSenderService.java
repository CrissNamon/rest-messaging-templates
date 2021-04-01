package ru.rassokhindanila.restmessagingtemplates.service.Impl;

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

    @Override
    public Mono<SenderResponse> send(Receiver receiver, Object data) throws SenderException {
        return null;
    }

    @Override
    public Flux<SenderResponse> send(Set<Receiver> receivers, Object data) throws SenderException {
        return null;
    }
}
