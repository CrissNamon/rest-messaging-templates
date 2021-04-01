package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.WebClientResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;
import ru.rassokhindanila.restmessagingtemplates.service.WebClientService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class WebClientServiceImpl implements WebClientService {

    private Logger logger;

    public WebClientServiceImpl()
    {
        logger = LoggerFactory.getLogger(WebClientServiceImpl.class);
    }

    @Override
    public Mono<WebClientResponse> post(String url, Object data) throws WebClientException {
        try {
            URI uri = new URI(url);
            WebClient webClient = WebClient.create(uri.toString());
            return webClient
                    .post()
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(WebClientResponse.class);
        } catch (URISyntaxException e) {
            logger.error("Malformed url: "+url);
            throw new WebClientException("Malformed URL");
        } catch (org.springframework.web.reactive.function.client.WebClientException e)
        {
            logger.error(e.getMessage());
            throw new WebClientException(e.getMessage());
        }
    }

    @Override
    public Flux<WebClientResponse> postMany(HashMap<String, Object> data) throws WebClientException{
        List<Mono<WebClientResponse>> responseList = new ArrayList<>();
        for(String url : data.keySet())
        {
            responseList.add(post(url, data.get(url)));
        }
        return Flux.merge(responseList);
    }

    @Override
    public Flux<WebClientResponse> postMany(Set<String> urls, Object data) throws WebClientException{
        List<Mono<WebClientResponse>> responseList = new ArrayList<>();
        for(String url : urls)
        {
            logger.info("SENDING MESSAGE "+data.toString()+" TO "+url);
            responseList.add(post(url, data));
        }
        return Flux.merge(responseList);
    }
}
