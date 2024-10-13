package com.ecommerce.product.controlleradvice; // Ensure this package matches your structure

import com.ecommerce.product.exception.ProductAlreadyExistException;
import com.ecommerce.product.exception.ProductServiceCustomException;
import com.ecommerce.product.model.ResponseModel;
import com.ecommerce.product.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                .statusModel(StatusModel.builder().statusCode(400).statusMsg("BAD REQUEST").build())
                .build(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ResponseModel> handleProductServiceException(ProductAlreadyExistException ex) {
        // Log the exception details
        logger.error("ProductAlreadyExistException: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder().statusCode(409).statusMsg("CONFLICT").build())
                .build(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errorMessages = new HashMap<>();

        // Collect all validation error messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            // Check if error message relates to @NotNull or @Positive
            if (errorMessage.contains("cannot be null")) {
                errorMessage = fieldName + " is required and cannot be null.";
            } else if (errorMessage.contains("must be a positive value")) {
                errorMessage = fieldName + " must be a positive value.";
            }

            errorMessages.put(fieldName, errorMessage);
        });

        // Log the error messages
        logger.error("Validation errors: {}", errorMessages);

        StringBuilder responseMsg = new StringBuilder("");
        errorMessages.forEach((field, message) -> responseMsg.append("[").append(field).append(": ").append(message).append("], "));

        // Remove trailing comma and space
        responseMsg.setLength(responseMsg.length() - 2);

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(responseMsg.toString())
                .statusModel(StatusModel.builder().statusCode(400).statusMsg("BAD REQUEST").build())
                .build(), HttpStatus.BAD_REQUEST);
    }



}
