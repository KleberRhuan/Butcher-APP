package io.github.kleberrhuan.butcherapp.controllers;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.Cart;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartData;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartItemAdd;
import io.github.kleberrhuan.butcherapp.domain.records.cart.CartItemData;
import io.github.kleberrhuan.butcherapp.domain.repositories.CartRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.domain.services.CartServices;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import io.github.kleberrhuan.butcherapp.infra.security.jwt.JwtServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/carts")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartServices cartServices;
    private final CartRepository cartRepository;
    private final JwtServices jwtServices;
    private final UserRepository userRepository;

    @GetMapping("/my-cart")
    public ResponseEntity<CartData> getCart(@RequestHeader String authorization) {
        String userEmail = jwtServices.getUsernameFromHeader(authorization);
        User user =  userRepository.findByEmail(userEmail).orElseThrow(
            () -> new BadRequestException("User not found")
        );
        Cart cart = cartRepository.findByUser(user).orElse(
                cartServices.createCart(user)
        );
        return ResponseEntity.ok().body(new CartData(cart));
    }

    @PutMapping("/update/{userEmail}")
    public ResponseEntity<CartData> updateCart(@PathVariable String userEmail, @RequestBody CartItemAdd cartItemData) {
        CartData cartData = cartServices.updateCart(userEmail, cartItemData);
        return ResponseEntity.ok().body(cartData);
    }

    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<CartData> deleteCartItem(@PathVariable String userEmail,
                                                   @RequestBody Set<CartItemData> cartItemData) {
        cartServices.deleteCartItems(userEmail, cartItemData);
        return ResponseEntity.noContent().build();
    }

}
