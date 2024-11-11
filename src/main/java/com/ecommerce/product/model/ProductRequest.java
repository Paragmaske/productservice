package com.ecommerce.product.model;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Double price;
    private Long quantity;
}
