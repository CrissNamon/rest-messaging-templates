package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;
import ru.rassokhindanila.restmessagingtemplates.dto.SenderResponse;
import ru.rassokhindanila.restmessagingtemplates.enums.ReceiverType;
import ru.rassokhindanila.restmessagingtemplates.exception.SenderException;
import ru.rassokhindanila.restmessagingtemplates.service.SenderService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WebSenderService implements SenderService {

    private final Logger logger;

    public WebSenderService()
    {
        logger = LoggerFactory.getLogger(WebSenderService.class);
    }

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

    private Flux<SenderResponse> postMany(Set<String> urls, Object data) throws SenderException {
        List<Mono<SenderResponse>> responseList = new ArrayList<>();
        for(String url : urls)
        {
            logger.info("SENDING MESSAGE "+data.toString()+" TO "+url);
            try {
                responseList.add(post(url, data));
            }catch(WebClientException e)
            {
                logger.error("GOT EXCEPTION IN POSTMANY");
            }
        }
        return Flux.merge(responseList);
    }

    @Override
    public Mono<SenderResponse> send(Receiver receiver, Object data) throws SenderException {
        return post(receiver.getDestination(), data);
    }

    @Override
    public Flux<SenderResponse> send(Set<Receiver> receivers, Object data) throws SenderException {

        receivers = receivers.stream()
                .filter(this::canBeSent)
                .collect(Collectors.toSet());
        
        Set<String> urls = new HashSet<>();
        receivers.forEach(receiver -> urls.add(receiver.getDestination()));
        return postMany(urls, data);
    }

    @Override
    public boolean canBeSent(Receiver receiver) {
        return receiver.getReceiverType() == ReceiverType.POST;
    }


}
