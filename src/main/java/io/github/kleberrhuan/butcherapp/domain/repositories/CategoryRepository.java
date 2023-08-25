package io.github.kleberrhuan.butcherapp.domain.repositories;

import io.github.kleberrhuan.butcherapp.domain.entities.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Cacheable("categories")
    Page<Category> findAllByIsActiveTrue(Pageable pageable);

}
