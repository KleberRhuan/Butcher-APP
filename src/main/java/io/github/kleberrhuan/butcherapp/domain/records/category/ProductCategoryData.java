package io.github.kleberrhuan.butcherapp.domain.records.category;

public record ProductCategoryData(
        Long id,
        String name,
        String description
) {
    public ProductCategoryData(io.github.kleberrhuan.butcherapp.domain.entities.Category category) {
        this(category.getId(), category.getName(), category.getDescription());
    }
}
