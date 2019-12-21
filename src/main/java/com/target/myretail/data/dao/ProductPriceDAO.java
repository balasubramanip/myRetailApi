package com.target.myretail.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.target.myretail.data.entity.ProductPriceEntity;
import com.target.myretail.data.repository.ProductPriceRepository;
import com.target.myretail.exception.ProductNotFoundException;
import reactor.core.publisher.Mono;

@Component
public class ProductPriceDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceDAO.class);
    final Mono<ProductPriceEntity> fallback = Mono.error(new ProductNotFoundException());

    private ProductPriceRepository productPriceRepository;

    @Autowired
    public ProductPriceDAO(final ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    public Mono<ProductPriceEntity> findById(final Long productId) {
        LOGGER.debug("Getting Product Price from Database for Product ID {}", productId);
        return productPriceRepository.findById(productId)
                                     .switchIfEmpty(fallback);
    }

    public Mono<ProductPriceEntity> updateProductPrice(final ProductPriceEntity productPrice) {
        LOGGER.debug("Updating Product Price from Database for Product ID {}",
                    productPrice.getId());
        Mono<ProductPriceEntity> productPriceEntityMono = productPriceRepository.save(productPrice);
        return productPriceEntityMono.switchIfEmpty(fallback);
    }
}
