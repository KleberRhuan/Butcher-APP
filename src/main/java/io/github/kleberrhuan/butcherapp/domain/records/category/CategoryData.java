package io.github.kleberrhuan.butcherapp.domain.records.category;

import io.github.kleberrhuan.butcherapp.domain.entities.Category;
import io.github.kleberrhuan.butcherapp.domain.enums.category.CategoryOrder;

public record CategoryData(
        Long id,
        String name,
        String image,
        String description,
        Boolean isActive,
        CategoryOrder order,
        Long parentId
) {
    public CategoryData(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getImage(),
                category.getDescription(),
                category.getIsActive(),
                category.getOrder(),
                category.getParent() != null ? category.getParent().getId() : null
        );
    }
}
