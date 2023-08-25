package io.github.kleberrhuan.butcherapp.domain.records.cart;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.Cart;

import java.util.Set;

public record CartData(
    Long id,
    Set<CartItemData> cartItems
) {
    public CartData(Cart cart) {
        this(cart.getId(), cart.getCartItemsData());
    }
}
