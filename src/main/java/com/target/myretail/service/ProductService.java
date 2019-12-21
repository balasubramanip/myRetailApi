package com.target.myretail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.target.myretail.data.dao.ProductPriceDAO;
import com.target.myretail.data.entity.ProductPriceEntity;
import com.target.myretail.exception.ProductException;
import com.target.myretail.service.model.ProductInfo;
import com.target.myretail.service.model.ProductPrice;
import com.target.myretail.utils.ProductConstant;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductPriceDAO productPriceDAO;

    @Autowired
    private ProductNameService productNameServiceTemp;

    public Mono<ResponseEntity<ProductInfo>> getProduct(final Long productId) {
        LOGGER.debug("Getting Product Info for Product ID : {}",
                     productId);

        final Mono<ProductPriceEntity> productPriceEntityMono = productPriceDAO.findById(productId);
        final Mono<String> productNameMono = productNameServiceTemp.getProductName(productId);
        final Mono<ResponseEntity<ProductInfo>> responseEntityMono = aggregateProductDetails(productId,
                                                                                             productNameMono,
                                                                                             productPriceEntityMono);
        LOGGER.debug("Successfully retrieved Product Info for Product ID : {}",
                     productId);
        return responseEntityMono;
    }

    public Mono<ResponseEntity<ProductInfo>> updateProduct(final Long productId,
                                                           final ProductInfo productInfo) {
        LOGGER.debug("Updating Product Price for Product ID : {}",
                     productId);
        if (!productId.equals(productInfo.getProductId())) {
            return Mono.error(new ProductException(ProductConstant.PRODUCT_ID_MISMATCH));
        }

        Mono<ProductPriceEntity> productPriceEntityMono = productPriceDAO.findById(productId);
        productPriceEntityMono = productPriceEntityMono.flatMap(productPriceEntity -> updatePriceinDB(productInfo,
                                                                                                  productPriceEntity));
        final Mono<ResponseEntity<ProductInfo>> responseEntityMono = createProductResponse(productInfo,
                                                                                        productPriceEntityMono);
        LOGGER.debug("Successfully Updated Product Price for Product ID : {}",
                     productId);
        return responseEntityMono;
    }

    private Mono<ProductPriceEntity> updatePriceinDB(final ProductInfo productInfo,
                                                 final ProductPriceEntity productPriceEntity) {
        LOGGER.info("Found Product Information, Updating price");
        productPriceEntity.setCurrencyCode(productInfo.getProductPrice()
                                                       .getCurrencyCode());
        productPriceEntity.setPrice(productInfo.getProductPrice()
                                               .getPrice());
        return productPriceDAO.updateProductPrice(productPriceEntity);
    }

    private Mono<ResponseEntity<ProductInfo>> aggregateProductDetails(final Long productId,
                                                                      final Mono<String> productNameResponseMono,
                                                                      final Mono<ProductPriceEntity> productPriceEntityMono) {

        return Mono.zip(productPriceEntityMono,
                        productNameResponseMono)
                   .map(tuple -> {
                       final ProductPriceEntity productPriceEntity = tuple.getT1();
                       final String productName = tuple.getT2();
                       final ProductInfo productInfo = new ProductInfo();
                       productInfo.setProductId(productId);
                       productInfo.setName(productName);
                       return getProductInfoResponseEntity(productPriceEntity,
                                                           productInfo);
                   });

    }

    private Mono<ResponseEntity<ProductInfo>> createProductResponse(final ProductInfo productInfo,
                                                                 final Mono<ProductPriceEntity> productPriceEntityMono) {
        return productPriceEntityMono.map(productPriceEntity -> getProductInfoResponseEntity(productPriceEntity,
                                                                                             productInfo));
    }

    private ResponseEntity<ProductInfo> getProductInfoResponseEntity(ProductPriceEntity productPriceEntity,
                                                                     ProductInfo productInfo) {
        productInfo.setProductPrice(new ProductPrice(productPriceEntity.getPrice(),
                                                     productPriceEntity.getCurrencyCode()));
        return new ResponseEntity<>(productInfo,
                                    HttpStatus.OK);
    }
}
