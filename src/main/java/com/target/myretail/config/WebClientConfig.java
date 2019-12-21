package com.target.myretail.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.target.myretail.data.entity.ProductPriceEntity;
import com.target.myretail.data.repository.ProductPriceRepository;
import com.target.myretail.utils.ProductConstant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientConfig.class);

    @Bean(name = "redSkyClient")
    public WebClient webClient(Environment environment) {
        return WebClient.builder()
                        .baseUrl(environment.getProperty(ProductConstant.REDSKU_URI))
                        .defaultHeader("Content-Type",
                                       MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader("Accept",
                                       MediaType.APPLICATION_JSON_VALUE)
                        .filter(logRequest())
                        .build();
    }

    // This method returns filter function which will log request data
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: {} {}",
                        clientRequest.method(),
                        clientRequest.url());
            clientRequest.headers()
                         .forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}",
                                                                                        name,
                                                                                        value)));
            return Mono.just(clientRequest);
        });
    }

    @Bean
    CommandLineRunner commandLineRunner(ProductPriceRepository productPriceRepository) {
        return strings -> {
            productPriceRepository.deleteAll();
            List<ProductPriceEntity> list = new ArrayList<ProductPriceEntity>();

            addToList(list,
                      13860428L,
                      new BigDecimal("299.99"),
                      "USD");
            addToList(list,
                      13860500L,
                      new BigDecimal("399.99"),
                      "USD");
            addToList(list,
                      13860600L,
                      new BigDecimal("99.99"),
                      "USD");
            productPriceRepository.saveAll(list).blockLast();
            final Flux<ProductPriceEntity> productPriceEntityFlux = productPriceRepository.findAll();
            productPriceEntityFlux.collectList().subscribe();
        };
    }

    private void addToList(List<ProductPriceEntity> list,
                           Long productId,
                           BigDecimal price,
                           String currency) {
        list.add(new ProductPriceEntity(productId,
                                        price,
                                        currency));
    }
}

