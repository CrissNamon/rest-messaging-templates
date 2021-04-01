package ru.rassokhindanila.restmessagingtemplates.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.WebClientResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;

import java.util.Set;

/**
 * Service for making web requests
 */
public interface WebClientService {

    /**
     * Sends post request to url
     * @param url Endpoint url
     * @param data Data to send
     * @return Response of request
     * @throws WebClientException Raised if an error occurred during request
     */
    Mono<WebClientResponse> post(String url, Object data) throws WebClientException;

    /**
     * Sends one data to many endpoints
     * @param urls Set of endpoints
     * @param data Data to send
     * @return Responses from all requests
     * @throws WebClientException Raised if an error occurred during request
     */
    Flux<WebClientResponse> postMany(Set<String> urls, Object data) throws WebClientException;
}
