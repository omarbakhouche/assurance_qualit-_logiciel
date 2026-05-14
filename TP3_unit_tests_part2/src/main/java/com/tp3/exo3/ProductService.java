package com.tp3.exo3;

import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service layer providing Product-related operations.
 * 
 * Fetches data from a local database instead of an external API.
 * Uses Testcontainers for MySQL interaction during testing phases.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be null or blank");
        }

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
    }

    public Product saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        productRepository.deleteById(productId);
    }

    // --- Exceptions ---

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidProductDataException extends RuntimeException {
        public InvalidProductDataException(String message) {
            super(message);
        }
        public InvalidProductDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}