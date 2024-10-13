package com.ecommerce.product.serviceImpl;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductAlreadyExistException;
import com.ecommerce.product.exception.ProductServiceCustomException;
import com.ecommerce.product.model.ProductRequest;
import com.ecommerce.product.model.ProductResponse;
import com.ecommerce.product.model.ResponseModel;
import com.ecommerce.product.model.StatusModel;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
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

    @Override
    public ResponseModel addProduct(ProductRequest productRequest) {
        logger.info("Entering addProduct method with request: {}", productRequest);
        Product product = buildProductFromRequest(productRequest);
        Product savedProduct = new Product();

        Optional<Product> productExist = productRepository.findByProductName(product.getProductName());
        if (productExist.isPresent()) {
            throw new ProductAlreadyExistException("Product named " + product.getProductName() + " already exists", "ALREADY_EXISTS");
        }

        try {
            savedProduct = productRepository.save(product);
            logger.info("Exiting addProduct method. Product ID: {}", savedProduct.getProductId());

        } catch (Exception e) {
            logger.error("Error occurred while adding product: {}", e.getMessage(), e);


        }
       return ResponseModel.builder().statusModel(StatusModel.builder().statusCode(201).statusMsg("CREATED").build()).responseMsg("PRODUCT ADDED").responseModel(generateResponseFromProduct(savedProduct)).build();


    }
    @Override
    public ResponseModel getProductById(long productId) {
        ProductResponse response ;
        logger.info("Entering getProductById method with productId: {}", productId);
            Product product = checkIfProductExists(productId);
            response = generateResponseFromProduct(product);
            logger.info("Exiting getProductById method. Response: {}", response);
        return ResponseModel.builder().statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build()).responseMsg("PRODUCT FOUND").responseModel(response).build();
    }

    private Product checkIfProductExists(long productId) {
        logger.info("Checking if product exists with productId: {}", productId);
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    String errorMessage = "Product with id " + productId + " not found";
                    logger.error(errorMessage);
                    return new ProductServiceCustomException(errorMessage, "PRODUCT_NOT_FOUND");
                });
    }

    @Override
    public ResponseModel updateProduct(long productId, ProductRequest productRequest) {
        logger.info("Entering updateProduct method with productId: {} and request: {}", productId, productRequest);

            Product product = checkIfProductExists(productId);
            try
            {
            product.setProductName(productRequest.getName());
            product.setQuantity(productRequest.getQuantity());
            product.setPrice(productRequest.getPrice());
            Product updatedProduct = productRepository.save(product);
            logger.info("Exiting updateProduct method. Updated product: {}", updatedProduct);

                return ResponseModel.builder().statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build()).responseMsg("PRODUCT UPDATED").responseModel(generateResponseFromProduct(updatedProduct)).build();


        } catch (Exception e) {
            logger.error("Error occurred while updating product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to update product", "DATABASE_ERROR");
        }
    }

    @Override
    public ResponseModel deleteProduct(long productId) {
        logger.info("Entering deleteProduct method with productId: {}", productId);

            Product product = checkIfProductExists(productId);
            try
            {
       productRepository.delete(product);
            logger.info("Exiting deleteProduct method. Deleted productId: {}", productId);
                return ResponseModel.builder().statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build()).responseMsg("PRODUCT DELETED").responseModel(generateResponseFromProduct(product)).build();

            } catch (Exception e) {
            logger.error("Error occurred while deleting product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to delete product", "DATABASE_ERROR");
        }
    }

    @Override
    public ResponseModel patchProduct(long productId, ProductRequest productRequest) {
        logger.info("Entering patchProduct method with productId: {} and request: {}", productId, productRequest);

            Product product = checkIfProductExists(productId);

            if (productRequest.getName() != null) {
                product.setProductName(productRequest.getName());
            }
            if (productRequest.getPrice()!=null && productRequest.getPrice()>0)  {
                product.setPrice(productRequest.getPrice());
            }
            if( productRequest.getQuantity()!=null  && productRequest.getQuantity()>0) {
                product.setQuantity(productRequest.getQuantity());
            }
        try{
            Product patchedProduct = productRepository.save(product);
            logger.info("Exiting patchProduct method. Patched product: {}", patchedProduct);

            return ResponseModel.builder().statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build()).responseMsg("PRODUCT PATCHED").responseModel(generateResponseFromProduct(patchedProduct)).build();


        } catch (Exception e) {
            logger.error("Error occurred while patching product: {}", e.getMessage(), e);
            throw new ProductServiceCustomException("Failed to patch product", "DATABASE_ERROR");
        }
    }

    @Override
    public ProductResponse reduceQuantity(long productId, long quantity) {
        Product product = checkIfProductExists(productId);

        if(product.getQuantity()<quantity)
        {
            throw new ProductServiceCustomException("Insufficient quantity for productId "+product.getProductId()+" Quantity placed: "+quantity+" quantity in inventory: "
                    +product.getQuantity(),"INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity()-quantity);
        Product updatedProduct = productRepository.save(product);

        return  generateResponseFromProduct(updatedProduct);
    }

    @Override
    public ProductResponse revertQuantity(long productId, long quantity) {
        Product product = checkIfProductExists(productId);
        product.setQuantity(product.getQuantity()+quantity);
        Product updatedProduct = productRepository.save(product);
        return  generateResponseFromProduct(updatedProduct);
    }

    private ProductResponse generateResponseFromProduct(Product product) {


        return ProductResponse.builder().productName(product.getProductName())
                .productId(product.getProductId()).price(product.getPrice()).quantity(product.getQuantity()).build();
    }

    private Product buildProductFromRequest(ProductRequest productRequest) {
        return Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(Math.round(productRequest.getPrice() * 100.0) / 100.0)
                .build();
    }
}
