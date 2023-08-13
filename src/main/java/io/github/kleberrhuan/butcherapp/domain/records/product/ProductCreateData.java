package io.github.kleberrhuan.butcherapp.domain.records.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ProductCreateData(
        @NotBlank
        String name,
        @NotNull
        @Size(min=1, max=10)
        HashSet<String> images,
        @NotNull
        @Min(1)
        Integer stock,
        @NotNull
        @Min(1)
        Double price,
        @NotBlank
        String description
) {
}
