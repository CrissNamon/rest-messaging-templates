package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.functional.VoidParamFunctional;
import ru.rassokhindanila.restmessagingtemplates.service.LoggerService;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WebSenderService implements SenderService {

    @Autowired
    private LoggerService logger;

    /**
     * Makes post request
     * @param url Destination url
     * @param data Object to send
     * @return Response of request
     * @throws SenderException Raised if an error occurred
     */
    private Mono<SenderResponse> post(String url, Object data) throws SenderException {
        try {
            URL parseUrl = new URL(url);
            WebClient webClient = WebClient.create(parseUrl.toString());
            return webClient
                    .post()
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(SenderResponse.class);
        } catch (WebClientException e)
        {
            logger.error(e.getMessage());
            throw new SenderException(e.getMessage());
        } catch (MalformedURLException e) {
            logger.error("Error occurred during sending message. Malformed url: " + url);
            throw new SenderException("Malformed URL");
        }
    }

    /**
     * Makes post requests
     * @param urls Destination urls
     * @param data Object to send
     * @return Response of request
     */
    private Flux<SenderResponse> postMany(Set<String> urls, Object data) {
        List<Mono<SenderResponse>> responseList = new ArrayList<>();
        for(String url : urls)
        {
            logger.info("SENDING MESSAGE "+data.toString()+" TO "+url);
            try {
                Mono<SenderResponse> responseMono = post(url, data);
                responseList.add(
                        responseMono
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
    public void send(Receiver receiver, Object data, VoidParamFunctional<SenderResponse> onResponse) throws SenderException {
        post(receiver.getDestination(), data)
                .subscribe(
                        onResponse::action
                );
    }

    @Override
    public void send(Set<Receiver> receivers, Object data, VoidParamFunctional<SenderResponse> onResponse) {
        receivers = filterValidReceivers(receivers);
        Set<String> urls = new HashSet<>();
        receivers.forEach(receiver -> urls.add(receiver.getDestination()));
        postMany(urls, data)
                .subscribe(
                        onResponse::action
                );
    }

    @Override
    public boolean canBeSent(Receiver receiver) {
        return receiver.getReceiverType() == ReceiverType.POST;
    }

    @Override
    public Set<Receiver> filterValidReceivers(Set<Receiver> receivers) {
        return receivers.stream()
                .filter(this::canBeSent)
                .collect(Collectors.toSet());
    }


}
