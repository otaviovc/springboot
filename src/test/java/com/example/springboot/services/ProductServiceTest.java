package com.example.springboot.services;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.exceptions.DatabaseConstraintViolationException;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRecordDto productRecordDto;
    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productRecordDto = new ProductRecordDto("Product A", BigDecimal.valueOf(100.0));
        product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        product.setProductId(productId);
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(productRecordDto);

        assertNotNull(savedProduct);
        assertEquals(productId, savedProduct.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(productId);

        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getProductId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(productId, productRecordDto);

        assertNotNull(updatedProduct);
        assertEquals(productId, updatedProduct.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldDeleteProductSuccessfully() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_WhenConstraintViolation_ShouldThrowException() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doThrow(DataIntegrityViolationException.class).when(productRepository).delete(product);

        assertThrows(DatabaseConstraintViolationException.class, () -> productService.deleteProduct(productId));
        verify(productRepository, times(1)).delete(product);
    }
}
