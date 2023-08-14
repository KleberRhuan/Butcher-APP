package io.github.kleberrhuan.butcherapp.domain.records.category;

import io.github.kleberrhuan.butcherapp.domain.enums.category.CategoryOrder;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateData(
        @NotBlank
        String name,
        @NotBlank
        String image,
        @NotBlank
        String description,
        @NotNull
        CategoryOrder order,
        @Nullable
        Long parentId
) {
}
