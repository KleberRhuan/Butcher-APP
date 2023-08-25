package io.github.kleberrhuan.butcherapp.domain.records.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.kleberrhuan.butcherapp.domain.entities.Category;
import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import io.github.kleberrhuan.butcherapp.domain.records.category.CategoryData;
import io.github.kleberrhuan.butcherapp.domain.records.category.ProductCategoryData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ProductData(
        Long id,
        String name,
        HashSet<String> images,
        HashSet<ProductCategoryData> categories,
        Integer stock,
        Double price,
        String description
) {
    public ProductData(Product product) {
        this(product.getId(), product.getName(), new HashSet<>(product.getImages()), new HashSet<>(product.getCategories()), product.getStock(),product.getPrice() / 100, product.getDescription());
    }

}
