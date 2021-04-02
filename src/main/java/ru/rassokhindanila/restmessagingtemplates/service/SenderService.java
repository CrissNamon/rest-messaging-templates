package ru.rassokhindanila.restmessagingtemplates.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;

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
    void send(Receiver receiver, Object data, VoidParamFunctional<SenderResponse> onResponse) throws SenderException;

    /**
     * @param receivers Set of receiver objects
     * @param data Data to send
     * @return Response of request
     * @throws SenderException Raised if an error occurred during request
     */
    void send(Set<Receiver> receivers, Object data, VoidParamFunctional<SenderResponse> onResponse) throws SenderException;


    /**
     * @param receiver Message receiver
     * @return true if message can be sent to receiver of specified type
     */
    boolean canBeSent(Receiver receiver);

    /**
     * @param receivers Set of receiver
     * @return Set of valid receivers for this Service only
     */
    Set<Receiver> filterValidReceivers(Set<Receiver> receivers);

}
