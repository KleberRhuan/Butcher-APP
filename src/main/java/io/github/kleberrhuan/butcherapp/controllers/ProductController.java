package io.github.kleberrhuan.butcherapp.controllers;

import io.github.kleberrhuan.butcherapp.domain.records.product.ProductData;
import io.github.kleberrhuan.butcherapp.domain.repositories.ProductRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts() {
        var products = productRepository.findAll().stream().map(ProductData::new).toList();
        Map<String, Object> productsBody = new HashMap<>();
        productsBody.put("products", products);
        return ResponseEntity.ok().body(productsBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).map(ProductData::new).orElseThrow(
                () -> new BadRequestException("Product not found")
        );
        return ResponseEntity.ok().body(product);
    }

}
