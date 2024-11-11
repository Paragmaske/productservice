package com.ecommerce.product.constants;

public class ResponseConstants {
    public static final int CREATED_CODE = 201;
    public static final String CREATED_MESSAGE = "CREATED";

    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MESSAGE = "SUCCESS";

    public static final int ALREADY_EXISTS_CODE = 409;
    public static final String ALREADY_EXISTS_MESSAGE = "ALREADY EXISTS";

    public static final int NOT_FOUND_CODE = 404;
    public static final String NOT_FOUND_MESSAGE = "PRODUCT NOT FOUND";

    public static final int DATABASE_ERROR_CODE = 500;
    public static final String DATABASE_ERROR_MESSAGE = "DATABASE ERROR";

    public static final int INSUFFICIENT_QUANTITY_CODE = 400;
    public static final String INSUFFICIENT_QUANTITY_MESSAGE = "INSUFFICIENT QUANTITY";

    public static final int BAD_REQUEST_CODE = 400;
    public static final String BAD_REQUEST_MESSAGE = "BAD REQUEST";

    public static final int UNAUTHORIZED_CODE = 401;
    public static final String UNAUTHORIZED_MESSAGE = "UNAUTHORIZED";

    public static final int FORBIDDEN_CODE = 403;
    public static final String FORBIDDEN_MESSAGE = "FORBIDDEN";

    public static final int INTERNAL_SERVER_ERROR_CODE = 500;
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "INTERNAL SERVER ERROR";

    // New response messages
    public static final String PRODUCT_FOUND_MESSAGE = "PRODUCT FOUND";
    public static final String PRODUCT_UPDATED_MESSAGE = "PRODUCT UPDATED";
    public static final String PRODUCT_DELETED_MESSAGE = "PRODUCT DELETED";
    public static final String PRODUCT_PATCHED_MESSAGE = "PRODUCT PATCHED";
    public static final String QUANTITY_REDUCED_MESSAGE = "Quantity reduced by ";
    public static final String QUANTITY_INCREASED_MESSAGE = "Quantity increased by ";
}
