package com.lgcns.admin.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${admin.ui.interceptor.ignore-urls}")
    private String[] ignoreUrls;

    @Value("${webclient.connection-timeout}")
    private int connectionTimeout;

    @Value("${webclient.max-connections}")
    private int maxConnections;

    @Value("${webclient.max-idle-time}")
    private int maxIdleTime;

    @Value("${webclient.max-life-time}")
    private int maxLifeTime;

    @Value("${webclient.pending-acquire-timeout}")
    private int pendingAcquireTimeout;

    @Value("${webclient.pending-acquire-maxCount}")
    private int pendingAcquireMaxCount;

    @Value("${webclient.evict-in-background}")
    private int evictInBackground;

    @Value("${webclient.metrics}")
    private boolean isMetrics;

    @Value("${webclient.response-timeout}")
    private int responseTimeout;

    @Value("${webclient.read-timeout}")
    private int readTimeout;

    @Value("${webclient.write-timeout}")
    private int writeTimeout;

    @Value("${webclient.max-in-memory-size}")
    private int maxInMemorySize;

    @Bean
    public WebClient getWebClient() {

        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom-provider")
                .maxConnections(this.maxConnections)
                .maxIdleTime(Duration.ofSeconds(this.maxIdleTime))
                .maxLifeTime(Duration.ofSeconds(this.maxLifeTime))
                .pendingAcquireTimeout(Duration.ofMillis(this.pendingAcquireTimeout))
                .pendingAcquireMaxCount(this.pendingAcquireMaxCount)
                .evictInBackground(Duration.ofSeconds(this.evictInBackground))
                .lifo()
                .metrics(this.isMetrics)
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectionTimeout)
                .responseTimeout(Duration.ofMillis(this.responseTimeout))
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(this.readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(this.writeTimeout, TimeUnit.MILLISECONDS)));

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(this.maxInMemorySize * 1024 * 1024)) // to unlimited memory size(-1_, 10MB (10 * 1024 * 1024)
                .build();

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                        clientRequest -> {
                            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                            clientRequest.headers()
                                    .forEach((name, values) -> values.forEach(value -> log.debug("Header-> {} : {}", name, value)));
                            return Mono.just(clientRequest);
                        }
                ))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        clientResponse -> {
                            clientResponse.headers()
                                    .asHttpHeaders()
                                    .forEach((name, values) ->
                                            values.forEach(value -> log.debug("Header-> {} : {}", name, value)));
                            return Mono.just(clientResponse);
                        }
                ))
                .build();

        return client;
    }
}
