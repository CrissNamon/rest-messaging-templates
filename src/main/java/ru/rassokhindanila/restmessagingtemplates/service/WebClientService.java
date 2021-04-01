package ru.rassokhindanila.restmessagingtemplates.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rassokhindanila.restmessagingtemplates.dto.WebClientResponse;
import ru.rassokhindanila.restmessagingtemplates.exception.WebClientException;

import java.util.HashMap;
import java.util.Set;

public interface WebClientService {
    Mono<WebClientResponse> post(String url, Object data) throws WebClientException;
    Flux<WebClientResponse> postMany(HashMap<String, Object> data) throws WebClientException;
    Flux<WebClientResponse> postMany(Set<String> urls, Object data) throws WebClientException;
}
