package com.soat_tech_challenge4.app_order.api.rest.client;

import com.soat_tech_challenge4.app_order.api.rest.dto.response.ExternalProductResponse;
import com.soat_tech_challenge4.app_order.application.exceptions.ExternalApiClientException;
import com.soat_tech_challenge4.app_order.application.exceptions.ExternalApiServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductApiClientImpl {

    private final WebClient webClient;

    public ProductApiClientImpl(@Qualifier("externalProductWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public ExternalProductResponse getByDocument(String document) {
        return webClient.get()
                .uri("/products/id/{document}", document)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new ExternalApiClientException("Error 400 when call api"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ExternalApiServerException("Error 500 when call api"))
                )
                .bodyToMono(ExternalProductResponse.class)
                .block(); // <-- importante
    }
}
