package io.github.kleberrhuan.butcherapp.domain.services;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.Cart;
import io.github.kleberrhuan.butcherapp.domain.entities.Cart.CartItem;
import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartData;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartItemAdd;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartItemData;
import io.github.kleberrhuan.butcherapp.domain.repositories.CartRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.ProductRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class CartServices {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart createCart(User user) {
        Cart cart = Cart.builder()
            .user(user)
            .build();
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public void deleteCart(Cart cartData) {
        var cart = cartRepository.findById(cartData.getId()).orElseThrow(
            () -> new BadRequestException("Cart not found")
        );
        cart.setCartItems(null);
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCartItems(String userEmail, Set<CartItemData> cartItemData) {
        var user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new BadRequestException("User not found")
        );
        var cart = cartRepository.findByUser(user).orElseThrow(
                () -> new BadRequestException("Cart not found")
        );
        for (CartItemData cartItem : cartItemData) {
            cart.getCartItems().removeIf(item -> item.getId().equals(cartItem.id()));
            cartRepository.save(cart);
        }
    }

    @Transactional
    public Cart addCartItem(User user, CartItemAdd cartItemData) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(
            () -> new BadRequestException("Cart not found")
        );
        Product product = productRepository.findById(cartItemData.productId()).orElseThrow(
            () -> new BadRequestException("Product not found")
        );

        CartItem cartItem = CartItem.builder()
            .cart(cart)
            .product(product)
            .quantity(cartItemData.quantity())
            .build();

        if(cart.getCartItems().contains(cartItem)) {
            cart.getCartItems().stream()
                .filter(item -> item.equals(cartItem))
                .findFirst()
                .ifPresent(item -> item.setQuantity(item.getQuantity() + cartItem.getQuantity()));
        } else {
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public CartData updateCart(String userEmail, CartItemAdd cartItemData) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
            () -> new BadRequestException("User not found")
        );
       Cart cart = addCartItem(user, cartItemData);
        return new CartData(cart);
    }
}
