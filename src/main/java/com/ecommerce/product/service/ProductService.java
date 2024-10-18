package com.ecommerce.product.service;


import com.ecommerce.product.model.ProductRequest;

import com.ecommerce.product.model.ResponseModel;
import jakarta.validation.Valid;

public interface ProductService {

     ResponseModel addProduct(ProductRequest productRequest);


    ResponseModel getProductById(long productId);

    ResponseModel updateProduct(long productId, @Valid ProductRequest productRequest);

    ResponseModel deleteProduct(long productId);

    ResponseModel patchProduct(long productId, ProductRequest productRequest);

    ResponseModel reduceQuantity(long productId, long quantity);

    ResponseModel revertQuantity(long productId, long quantity);
}
