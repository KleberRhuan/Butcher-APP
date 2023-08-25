package io.github.kleberrhuan.butcherapp.domain.services;

import io.github.kleberrhuan.butcherapp.domain.entities.Category;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.enums.category.CategoryOrder;
import io.github.kleberrhuan.butcherapp.domain.records.category.CategoryCreateData;
import io.github.kleberrhuan.butcherapp.domain.records.category.CategoryData;
import io.github.kleberrhuan.butcherapp.domain.repositories.CategoryRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.ForbiddenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class  CategoryServices {

    private final CategoryRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryData create(CategoryCreateData data, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ForbiddenException("User not found")
        );
        Category category = Category.builder()
                .name(data.name())
                .description(data.description())
                .image(data.image())
                .order(data.order())
                .createdBy(user)
                .build();

        if(data.order() != CategoryOrder.FIRST && data.parentId() != null) {
            Category parent = repository.findById(data.parentId()).orElseThrow(
                    () -> new ForbiddenException("Parent category not found")
            );
            category.setParent(parent);
        }

            repository.save(category);

        return new CategoryData(category);
    }


}
