package io.github.kleberrhuan.butcherapp.domain.records.cart;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.CartItem;
import io.github.kleberrhuan.butcherapp.domain.entities.Cart.CartItemKey;

public record CartItemData(
        CartItemKey id,
        Long productId,
        Long cartId,
        Integer quantity
) {
    public CartItemData(CartItem cartItem) {
        this(cartItem.getId(), cartItem.getProduct().getId(), cartItem.getCart().getId(), cartItem.getQuantity());
    }
}
