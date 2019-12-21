package com.target.myretail.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.target.myretail.exception.ClientServerException;
import com.target.myretail.product.Item;
import com.target.myretail.product.Product;
import com.target.myretail.product.ProductDescription;
import com.target.myretail.product.ProductResponse;
import com.target.myretail.utils.ProductConstant;
import reactor.core.publisher.Mono;

@Service
public class ProductNameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductNameService.class);

    @Autowired
    @Qualifier("redSkyClient")
    private WebClient webClient;

    @Autowired
    private Environment environment;

    public Mono<String> getProductName(Long productId) {
        LOGGER.debug("ProductNameService::getProductName Product ID {}",
                    productId);
        RequestBodyUriSpec requestBodyUriSpec = webClient.method(HttpMethod.GET);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(environment.getProperty(ProductConstant.REDSKU_URI) + environment.getProperty(ProductConstant.REDSKU_PRODUCTNAME_URL))
                .buildAndExpand(productId);


        requestBodyUriSpec = (RequestBodyUriSpec) requestBodyUriSpec.uri(uri.toUriString());
        Mono<ProductResponse> productNameResponseMono = requestBodyUriSpec
                .retrieve()
                .bodyToMono(ProductResponse.class)

                .doOnNext(res -> LOGGER.debug("RedSky Get Call is Successful {}" ))
                .onErrorMap(WebClientResponseException.class,
                            this::mapException
                );
        return buildProductName(productNameResponseMono);
    }

    private Mono<String> buildProductName(final Mono<ProductResponse> productNameResponseMono) {

        return productNameResponseMono.flatMap(productNameResponse -> {
            Optional<String> optProductName = Optional.ofNullable(productNameResponse)
                                                      .map(ProductResponse::getProduct)
                                                      .map(Product::getItem)
                                                      .map(Item::getProductDescription)
                                                      .map(ProductDescription::getTitle);

            if (optProductName.isEmpty()) {
                return Mono.error(new ClientServerException(ProductConstant.PRODUCT_NOT_FOUND,
                                                            HttpStatus.NOT_FOUND));
            }

            return Mono.just(optProductName.get());
        });
    }

    private Exception mapException(final WebClientResponseException e) {

        if (e.getStatusCode()
             .is4xxClientError()) {
            return new ClientServerException(ProductConstant.PRODUCT_NOT_FOUND,
                                             e.getStatusCode());
        }

        if (e.getStatusCode()
             .is5xxServerError()) {
            return new ClientServerException("Error while getting data from Product Name.",
                                             e.getStatusCode());
        }

        return e;
    }
}
