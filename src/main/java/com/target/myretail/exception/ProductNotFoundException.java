package com.target.myretail.exception;

import com.target.myretail.utils.ProductConstant;

public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1483991355281974507L;

    public ProductNotFoundException() {
        super(ProductConstant.PRODUCT_NOT_FOUND);
    }

}
