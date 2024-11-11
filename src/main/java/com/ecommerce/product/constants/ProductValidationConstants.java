package com.ecommerce.product.constants;

public class ProductValidationConstants {
    public static final String EMPTY_NAME = "Product name cannot be null or empty";
    public static final String INVALID_NAME_LENGTH = "Product name must contain between 2 and 8 words";

    // Price validation constants
    public static final String NULL_PRICE = "Price cannot be null or empty";
    public static final String NEGATIVE_PRICE = "Price must be a positive value";

    public static final String NULL_QUANTITY = "Quantity cannot be null or empty";
    public static final String NEGATIVE_QUANTITY = "Quantity must be a positive value";
}