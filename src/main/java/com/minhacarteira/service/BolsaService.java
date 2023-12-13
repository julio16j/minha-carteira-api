package com.minhacarteira.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.minhacarteira.model.dto.MarketPriceDTO;
import com.minhacarteira.model.dto.ResponseWrapper;

import reactor.core.publisher.Mono;

@Service
public class BolsaService {

    private final WebClient webClient;
    
    @Value("${brapi.base.url}")
    private String baseUrl;
    
    @Value("${brapi.token}")
    private String token;

    public BolsaService(WebClient.Builder webClientBuilder, @Value("${brapi.base.url}") String baseUrl) {
    	this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Double obterPrecoAtivo(String ticker) {
        return webClient
                .get()
                .uri(obterUriPrecoAtivo(ticker))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Erro ao obter preço do ativo " + ticker));
                })
                .bodyToMono(ResponseWrapper.class)
                .flatMap(responseWrapper -> {
                    if (responseWrapper.results() != null && !responseWrapper.results().isEmpty()) {
                        MarketPriceDTO firstResult = responseWrapper.results().get(0);
                        return Mono.just(firstResult.regularMarketPrice());
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        		"Erro ao obter preço do ativo: " + ticker));
                    }
                }).block();
    }
    
    public String obterUriPrecoAtivo (String ticker) {
    	String uri = "/quote/" + ticker + "?token=" + token;
    	return uri;
    }
}