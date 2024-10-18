package com.ecommerce.product.controller;

import com.ecommerce.product.model.ResponseModel;
import com.ecommerce.product.model.ProductRequest;

import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseModel> addProduct( @RequestBody ProductRequest productRequest) {
        logger.info("Entering addProduct with productRequest: {}", productRequest.toString());

        ResponseModel productResponse = productService.addProduct(productRequest);

        logger.info("Exiting addProduct with productResponse: {}", productResponse);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getProductById(@PathVariable("id") long productId) {
        logger.info("Entering getProductById with productId: {}", productId);

        ResponseModel productResponse = productService.getProductById(productId);

        logger.info("Exiting getProductById with productResponse: {}", productResponse);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel> updateProduct(@PathVariable("id") long productId,
                                                         @Valid @RequestBody ProductRequest productRequest) {
        logger.info("Entering updateProduct with productId: {} and productRequest: {}", productId, productRequest);

        ResponseModel updatedProduct = productService.updateProduct(productId, productRequest);

        logger.info("Exiting updateProduct with updatedProduct: {}", updatedProduct);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel> deleteProduct(@PathVariable("id") long productId) {
        logger.info("Entering deleteProduct with productId: {}", productId);

        ResponseModel deletedProduct = productService.deleteProduct(productId);

        logger.info("Exiting deleteProduct for product : {}", deletedProduct.toString());
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseModel> patchProduct(@PathVariable("id") long productId,
                                                        @RequestBody ProductRequest productRequest) {
        logger.info("Entering patchProduct with productId: {} and productRequest: {}", productId, productRequest);

        ResponseModel patchedProduct = productService.patchProduct(productId, productRequest);

        logger.info("Exiting patchProduct with patchedProduct: {}", patchedProduct);
        return new ResponseEntity<>(patchedProduct, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<ResponseModel> reduceQuantity(@PathVariable("id") long productId,@RequestParam long quantity) {
        ResponseModel productResponse = productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/revertQuantity/{id}")
    public ResponseEntity<ResponseModel> revertQuantity(@PathVariable("id") long productId,@RequestParam long quantity) {
        ResponseModel productResponse = productService.revertQuantity(productId, quantity);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

}
