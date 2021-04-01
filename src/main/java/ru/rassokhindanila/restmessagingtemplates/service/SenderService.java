package ru.rassokhindanila.restmessagingtemplates.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;

import java.util.Set;

/**
 * Service for sending data
 */
public interface SenderService {

    /**
     * @param receiver Receiver object
     * @param data Data to send
     * @return Response of request
     * @throws SenderException Raised if an error occurred during request
     */
    Mono<SenderResponse> send(Receiver receiver, Object data) throws SenderException;

    /**
     * @param receivers Set of receiver objects
     * @param data Data to send
     * @return Response of request
     * @throws SenderException Raised if an error occurred during request
     */
    Flux<SenderResponse> send(Set<Receiver> receivers, Object data) throws SenderException;

}
