package com.ecommerce.product.exception;

import lombok.Data;

@Data
public class ProductAlreadyExistException  extends RuntimeException{
    private String errorCode;

    public ProductAlreadyExistException(String message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }
}
