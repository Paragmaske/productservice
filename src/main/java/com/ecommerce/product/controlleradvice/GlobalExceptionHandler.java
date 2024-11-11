package com.ecommerce.product.controlleradvice;

import com.ecommerce.product.constants.ResponseConstants;
import com.ecommerce.product.exception.ProductAlreadyExistException;
import com.ecommerce.product.exception.ProductServiceCustomException;
import com.ecommerce.product.exception.ValidationException;
import com.ecommerce.product.model.ResponseModel;
import com.ecommerce.product.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ResponseModel> handleProductServiceException(ProductServiceCustomException ex) {
        // Log the exception details
        logger.error("ProductServiceCustomException: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.NOT_FOUND_CODE)
                        .statusMsg(ResponseConstants.NOT_FOUND_MESSAGE)
                        .build())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ResponseModel> handleProductAlreadyExistException(ProductAlreadyExistException ex) {
        // Log the exception details
        logger.error("ProductAlreadyExistException: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.ALREADY_EXISTS_CODE)
                        .statusMsg(ResponseConstants.ALREADY_EXISTS_MESSAGE)
                        .build())
                .build(), HttpStatus.CONFLICT);
    }




        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ResponseModel> handleValidationException(ValidationException ex) {
            // Log the exception details
            logger.error("ValidationException: {}", ex.getMessage(), ex);

            return new ResponseEntity<>(ResponseModel.builder()
                    .responseMsg(ex.getMessage())
                    .statusModel(StatusModel.builder()
                            .statusCode(ResponseConstants.BAD_REQUEST_CODE)
                            .statusMsg(ResponseConstants.BAD_REQUEST_MESSAGE)
                            .build())
                    .build(), HttpStatus.BAD_REQUEST);
        }


}

