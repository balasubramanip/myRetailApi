package com.target.myretail.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.target.myretail.service.ProductService;
import com.target.myretail.service.model.ProductInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/retail")
@Api(tags = "Product APIs")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Display Product Information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product Details. Ex ProductID.13860428"),
            @ApiResponse(code = 404, message = "Product Name Not Found"),
            @ApiResponse(code = 404, message = "Product Not Found")})
    @GetMapping(value = "/product/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<ProductInfo>> getProductDetails(@PathVariable(value = "id", required = true) @Valid Long productId) {
        return productService.getProduct(productId);
    }


    @ApiOperation(value = "Update Product Price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated Product Details. Ex ProductID.13860428"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Product Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PutMapping(value = "/product/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<ProductInfo>> updateProductDetails(@PathVariable(value = "id", required = true) Long productId,
                                                                  @Valid @RequestBody ProductInfo productInfo) {
        return productService.updateProduct(productId,
                                            productInfo);
    }
}
