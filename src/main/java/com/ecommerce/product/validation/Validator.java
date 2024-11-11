package com.ecommerce.product.validation;

import com.ecommerce.product.constants.ProductValidationConstants;
import com.ecommerce.product.exception.ValidationException;
import com.ecommerce.product.model.ProductRequest;


public  class Validator {
    public static void  validate(ProductRequest productRequest) {
        if ( productRequest.getName().isEmpty() ||  productRequest.getName().isBlank()) {
            throw new ValidationException(ProductValidationConstants.EMPTY_NAME, "INVALID_NAME");
        }
        if (productRequest.getPrice() == null) {
            throw new ValidationException(ProductValidationConstants.NULL_PRICE, "INVALID_PRICE");
        }
        if (productRequest.getPrice() <= 0) {
            throw new ValidationException(ProductValidationConstants.NEGATIVE_PRICE, "INVALID_PRICE");
        }
        if (productRequest.getQuantity() == null) {
            throw new ValidationException(ProductValidationConstants.NULL_QUANTITY, "INVALID_QUANTITY");
        }
        if (productRequest.getQuantity() <= 0) {
            throw new ValidationException(ProductValidationConstants.NEGATIVE_QUANTITY, "INVALID_QUANTITY");
        }
        if (productRequest.getName().length() < 2 || productRequest.getName().length() > 8) {
            throw new ValidationException(ProductValidationConstants.INVALID_NAME_LENGTH, "INVALID_NAME_LENGTH");
        }


    }}
