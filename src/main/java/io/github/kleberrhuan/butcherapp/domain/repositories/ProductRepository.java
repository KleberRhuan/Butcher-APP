package io.github.kleberrhuan.butcherapp.domain.repositories;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Cacheable("products")
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
}
