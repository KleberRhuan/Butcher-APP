package io.github.kleberrhuan.butcherapp.domain.entities.Cart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class CartItemKey implements Serializable {
        @Column(name = "cart_id")
        private Integer cartId;

        @Column(name = "product_id")
        private Integer productId;
    // getters, setters, hashCode, equals methods
    }

