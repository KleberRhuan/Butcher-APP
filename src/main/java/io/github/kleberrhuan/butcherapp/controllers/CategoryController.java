package io.github.kleberrhuan.butcherapp.controllers;

import io.github.kleberrhuan.butcherapp.domain.records.category.CategoryData;
import io.github.kleberrhuan.butcherapp.domain.repositories.CategoryRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;

    @GetMapping
    public ResponseEntity<List<CategoryData>> findAll(@PageableDefault(sort = "name")
                                                          Pageable pageable) {
        var categories = repository.findAllByIsActiveTrue(pageable)
                .map(CategoryData::new)
                .getContent();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryData> findById(@PathVariable Long id) {
        var category = repository.findById(id).map(CategoryData::new).orElseThrow(
                () -> new BadRequestException("Category not found")
        );
        return ResponseEntity.ok().body(category);
    }

}
