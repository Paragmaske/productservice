package com.ecommerce.product.service;


import com.ecommerce.product.model.ProductRequest;

import com.ecommerce.product.model.ResponseModel;
import jakarta.validation.Valid;

public interface ProductService {

     ResponseModel addProduct(ProductRequest productRequest);


    ResponseModel getProductById(Long productId);

    ResponseModel updateProduct(Long productId, ProductRequest productRequest);

    ResponseModel deleteProduct(Long productId);

    ResponseModel patchProduct(Long productId, ProductRequest productRequest);

    ResponseModel reduceQuantity(Long productId, Long quantity);

    ResponseModel revertQuantity(Long productId, Long quantity);
}
