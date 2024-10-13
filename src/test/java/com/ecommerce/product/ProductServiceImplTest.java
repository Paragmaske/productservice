//package com.ecommerce.product;
//
//
//import com.ecommerce.product.entity.Product;
//import com.ecommerce.product.exception.ProductServiceCustomException;
//import com.ecommerce.product.model.ProductRequest;
//import com.ecommerce.product.model.ProductResponse;
//import com.ecommerce.product.repository.ProductRepository;
//
//import com.ecommerce.product.serviceImpl.ProductServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class ProductServiceImplTest {
//
//    @InjectMocks
//    private ProductServiceImpl productService;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    private ProductRequest productRequest;
//    private Product product;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        productRequest = new ProductRequest();
//        productRequest.setName("Test Product");
//        productRequest.setPrice(100);
//        productRequest.setQuantity(10);
//
//        product = Product.builder()
//                .productId(1L)
//                .productName("Test Product")
//                .price(100)
//                .quantity(10)
//                .build();
//    }
//
//    @Test
//    public void addProduct_shouldReturnProductId() {
//
//
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        long productId = productService.addProduct(productRequest);
//        assertEquals(1L, productId);
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//
//
//    @Test
//    public void getProductById_shouldReturnProductResponse() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        ProductResponse response = productService.getProductById(1L);
//        assertEquals("Test Product", response.getProductName());
//        assertEquals(100, response.getPrice());
//        assertEquals(10, response.getQuantity());
//    }
//
//    @Test
//    public void getProductById_productNotFound_shouldThrowException() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        ProductServiceCustomException exception = assertThrows(ProductServiceCustomException.class, () -> {
//            productService.getProductById(1L);
//        });
//
//        assertEquals("DATABASE_ERROR", exception.getErrorCode());
//    }
//
//    @Test
//    public void updateProduct_shouldReturnUpdatedProductResponse() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        ProductResponse updatedResponse = productService.updateProduct(1L, productRequest);
//        assertEquals("Test Product", updatedResponse.getProductName());
//        assertEquals(100, updatedResponse.getPrice());
//        assertEquals(10, updatedResponse.getQuantity());
//    }
//
//    @Test
//    public void deleteProduct_shouldReturnProductId() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        long deletedProductId = productService.deleteProduct(1L);
//        assertEquals(1L, deletedProductId);
//        verify(productRepository, times(1)).delete(product);
//    }
//
//    @Test
//    public void patchProduct_shouldReturnPatchedProductResponse() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        ProductResponse patchedResponse = productService.patchProduct(1L, productRequest);
//        assertEquals("Test Product", patchedResponse.getProductName());
//        assertEquals(100, patchedResponse.getPrice());
//        assertEquals(10, patchedResponse.getQuantity());
//    }
//}
