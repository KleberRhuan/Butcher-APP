package io.github.kleberrhuan.butcherapp.domain.repositories;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.Cart;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
