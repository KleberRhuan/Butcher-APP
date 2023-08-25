package io.github.kleberrhuan.butcherapp.domain.records.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemAdd(
        @NotNull
        Long productId,
        @NotNull
        @Min(1)
        Integer quantity
) {

}
