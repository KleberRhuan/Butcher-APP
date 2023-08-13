package io.github.kleberrhuan.butcherapp.domain.records.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;

public record ProductUpdateData(
        String name,
        @Size(min=1, max=10)
        HashSet<String> images,
        @Min(1)
        Integer stock,
        @Min(1)
        Double price,
        String description
) {
}
