package io.github.kleberrhuan.butcherapp.domain.entities.Cart;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProduct {
    @EmbeddedId
    private CartItemKey id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;}
