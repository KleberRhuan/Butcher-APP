package io.github.kleberrhuan.butcherapp.controllers;

import io.github.kleberrhuan.butcherapp.domain.records.product.ProductData;
import io.github.kleberrhuan.butcherapp.domain.repositories.ProductRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductData>> getProducts(@PageableDefault(size = 6, sort = "name")
                                                             Pageable pageable) {
        var products = productRepository.findAllByIsActiveTrue(pageable)
                .map(ProductData::new)
                .getContent();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).map(ProductData::new).orElseThrow(
                () -> new BadRequestException("Product not found")
        );
        return ResponseEntity.ok().body(product);
    }

}
