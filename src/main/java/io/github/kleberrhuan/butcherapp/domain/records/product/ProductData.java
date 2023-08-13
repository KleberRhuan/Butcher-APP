package io.github.kleberrhuan.butcherapp.domain.records.product;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;

import java.util.HashSet;
import java.util.List;

public record ProductData(
        Long id,
        String name,
        HashSet<String> images,
        Integer stock,
        Double price,
        String description
) {
    public ProductData(Product product) {
        this(product.getId(), product.getName(), new HashSet<>(product.getImages()), product.getStock(), product.getPrice() / 100, product.getDescription());
    }



}
