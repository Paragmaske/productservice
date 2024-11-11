package com.ecommerce.product.serviceImpl;

import com.ecommerce.product.constants.ResponseConstants;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductAlreadyExistException;
import com.ecommerce.product.exception.ProductServiceCustomException;
import com.ecommerce.product.model.ProductRequest;
import com.ecommerce.product.model.ProductResponse;
import com.ecommerce.product.model.ResponseModel;
import com.ecommerce.product.model.StatusModel;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.product.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisService redisService;


    @Override
    public ResponseModel addProduct(ProductRequest productRequest) {
        logger.info("Entering addProduct method with request: {}", productRequest);
        Product product = buildProductFromRequest(productRequest);
        Product savedProduct;

        Optional<Product> productExist = productRepository.findByProductName(product.getProductName());
        if (productExist.isPresent()) {
            throw new ProductAlreadyExistException("Product named " + product.getProductName() + " already exists",
                    ResponseConstants.ALREADY_EXISTS_MESSAGE);
        }

        try {
            savedProduct = productRepository.save(product);
            logger.info("Exiting addProduct method. Product ID: {}", savedProduct.getProductId());
             redisService.set(savedProduct.getProductId(),savedProduct,1);
             logger.info("product "+savedProduct+"saved in redis ");
        } catch (Exception e) {
            logger.error("Error occurred while adding product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException(ResponseConstants.DATABASE_ERROR_MESSAGE, "DATABASE_ERROR");
        }

        return ResponseModel.builder()
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.CREATED_CODE)
                        .statusMsg(ResponseConstants.CREATED_MESSAGE)
                        .build())
                .responseMsg("PRODUCT ADDED")
                .responseModel(generateResponseFromProduct(savedProduct))
                .build();
    }
    /**/
    @Override
    public ResponseModel getProductById(Long productId) {
        logger.info("Entering getProductById method with productId: {}", productId);
        Product product = checkIfProductExists(productId);
        ProductResponse response = generateResponseFromProduct(product);
        logger.info("Exiting getProductById method. Response: {}", response);

        return ResponseModel.builder()
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.SUCCESS_CODE)
                        .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                        .build())
                .responseMsg(ResponseConstants.PRODUCT_FOUND_MESSAGE)
                .responseModel(response)
                .build();
    }

    private Product checkIfProductExists(Long productId) {
        logger.info("Checking if product exists with productId: {}", productId);
        Optional<Product> product = redisService.get(productId, Product.class);

        if(product.isPresent())
        {
            logger.info("Product "+product.get()+" found in redis for id "+productId);
            return product.get();
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    String errorMessage = "Product with id " + productId + " not found";
                    logger.error(errorMessage);
                    throw new ProductServiceCustomException(errorMessage, ResponseConstants.NOT_FOUND_MESSAGE);
                });
    }

    @Override
    public ResponseModel updateProduct(Long productId, ProductRequest productRequest) {
        logger.info("Entering updateProduct method with productId: {} and request: {}", productId, productRequest);

        Product product = checkIfProductExists(productId);
        try {
            patchFieldstoProduct(productRequest, product);
            Product updatedProduct = productRepository.save(product);
            logger.info("Exiting updateProduct method. Updated product: {}", updatedProduct);

            return ResponseModel.builder()
                    .statusModel(StatusModel.builder()
                            .statusCode(ResponseConstants.SUCCESS_CODE)
                            .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                            .build())
                    .responseMsg(ResponseConstants.PRODUCT_UPDATED_MESSAGE)
                    .responseModel(generateResponseFromProduct(updatedProduct))
                    .build();

        } catch (Exception e) {
            logger.error("Error occurred while updating product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to update product", ResponseConstants.DATABASE_ERROR_MESSAGE);
        }
    }

    private void patchFieldstoProduct(ProductRequest productRequest, Product product) {
        if (!productRequest.getName().isBlank() && !productRequest.getName().isEmpty()) {
            product.setProductName(productRequest.getName());
        }
        Optional.ofNullable(productRequest.getPrice())
                .filter(price -> price > 0)
                .ifPresent(product::setPrice);

        Optional.ofNullable(productRequest.getQuantity())
                .filter(quantity -> quantity > 0)
                .ifPresent(product::setQuantity);
    }

    @Override
    public ResponseModel deleteProduct(Long productId) {
        logger.info("Entering deleteProduct method with productId: {}", productId);

        Product product = checkIfProductExists(productId);
        try {
            productRepository.delete(product);
            logger.info("Exiting deleteProduct method. Deleted productId: {}", productId);
            return ResponseModel.builder()
                    .statusModel(StatusModel.builder()
                            .statusCode(ResponseConstants.SUCCESS_CODE)
                            .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                            .build())
                    .responseMsg(ResponseConstants.PRODUCT_DELETED_MESSAGE)
                    .responseModel(generateResponseFromProduct(product))
                    .build();

        } catch (Exception e) {
            logger.error("Error occurred while deleting product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to delete product", ResponseConstants.DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public ResponseModel patchProduct(Long productId, ProductRequest productRequest) {
        logger.info("Entering patchProduct method with productId: {} and request: {}", productId, productRequest);

        Product product = checkIfProductExists(productId);
        patchFieldstoProduct(productRequest, product);
        try {
            Product patchedProduct = productRepository.save(product);
            logger.info("Exiting patchProduct method. Patched product: {}", patchedProduct);

            return ResponseModel.builder()
                    .statusModel(StatusModel.builder()
                            .statusCode(ResponseConstants.SUCCESS_CODE)
                            .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                            .build())
                    .responseMsg(ResponseConstants.PRODUCT_PATCHED_MESSAGE)
                    .responseModel(generateResponseFromProduct(patchedProduct))
                    .build();

        } catch (Exception e) {
            logger.error("Error occurred while patching product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to patch product", ResponseConstants.DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public ResponseModel reduceQuantity(Long productId, Long quantity) {
        Product product = checkIfProductExists(productId);

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Insufficient quantity for productId " + product.getProductId() +
                    " Quantity placed: " + quantity + " quantity in inventory: " + product.getQuantity(),
                    ResponseConstants.INSUFFICIENT_QUANTITY_MESSAGE);
        }
        product.setQuantity(product.getQuantity() - quantity);
        Product updatedProduct = productRepository.save(product);

        return ResponseModel.builder()
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.SUCCESS_CODE)
                        .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                        .build())
                .responseMsg(ResponseConstants.QUANTITY_REDUCED_MESSAGE + quantity)
                .responseModel(generateResponseFromProduct(updatedProduct))
                .build();
    }

    @Override
    public ResponseModel revertQuantity(Long productId, Long quantity) {
        Product product = checkIfProductExists(productId);
        product.setQuantity(product.getQuantity() + quantity);
        Product updatedProduct = productRepository.save(product);

        return ResponseModel.builder()
                .statusModel(StatusModel.builder()
                        .statusCode(ResponseConstants.SUCCESS_CODE)
                        .statusMsg(ResponseConstants.SUCCESS_MESSAGE)
                        .build())
                .responseMsg(ResponseConstants.QUANTITY_INCREASED_MESSAGE + quantity)
                .responseModel(generateResponseFromProduct(updatedProduct))
                .build();
    }

    private ProductResponse generateResponseFromProduct(Product product) {
        return ProductResponse.builder()
                .productName(product.getProductName())
                .productId(product.getProductId())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    private Product buildProductFromRequest(ProductRequest productRequest) {
        return Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(Math.round(productRequest.getPrice() * 100.0) / 100.0)
                .build();
    }
}
