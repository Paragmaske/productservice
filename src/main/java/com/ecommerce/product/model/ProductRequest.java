package com.ecommerce.product.model;


import com.ecommerce.product.validator.StringLength;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;




@Data
public class ProductRequest {


    @NotEmpty(message = "Product name cannot be null or empty")
    @StringLength(min = 2, max = 8, message = "Product name must contain between 2 and 8 words")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    private Double price;


    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive value")
    private Long quantity;
}
