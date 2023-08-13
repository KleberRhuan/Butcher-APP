package io.github.kleberrhuan.butcherapp.domain.repositories;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
