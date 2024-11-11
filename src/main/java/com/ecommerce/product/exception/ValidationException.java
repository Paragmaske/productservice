package com.ecommerce.product.exception;

public class ValidationException extends RuntimeException {
  private String errorCode;

    public ValidationException(String message,String errorCode) {
        super(message);
      this.errorCode=errorCode;
    }
}
