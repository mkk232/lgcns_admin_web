package com.lgcns.admin.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class WebService {
    private final WebClient webClient;

    public WebService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${admin.ui.search.url}")
    private String searchUrl;

    public Map<String, Object> requestSearch(String query) {
        Mono<Map<String, Object>> response =
                this.webClient
                        .post()
                        .uri(this.searchUrl)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .bodyValue(query)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseMap = response.block();
        log.trace("responseMap: {}", responseMap);

        return responseMap;
    }
}
