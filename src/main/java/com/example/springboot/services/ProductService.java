package com.example.springboot.services;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.exceptions.DatabaseConstraintViolationException;
import com.example.springboot.exceptions.ResourceNotFoundException;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(ProductRecordDto productRecordDto) {
        var product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public Product updateProduct(UUID id,  ProductRecordDto productRecordDto) {
        var product = getProductById(id);
        BeanUtils.copyProperties(productRecordDto, product);
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        try {
            var product = getProductById(id);
            productRepository.delete(product);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConstraintViolationException("Cannot delete product because there are orders associated with it.");
        }
    }
}
