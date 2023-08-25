package io.github.kleberrhuan.butcherapp.domain.services;

import io.github.kleberrhuan.butcherapp.domain.entities.Category;
import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductCreateData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductUpdateData;
import io.github.kleberrhuan.butcherapp.domain.repositories.CategoryRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.ProductRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.ForbiddenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ProductServices {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductData createProduct(ProductCreateData data, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ForbiddenException("User not found")
        );
        Product product = Product.builder()
                .name(data.name())
                .images(new ArrayList<>(data.images()))
                .stock(data.stock())
                .price(convertToCents(data.price()))
                .description(data.description())
                .createdBy(user)
                .build();

        productRepository.save(product);
        return new ProductData(product);
    }

    private Double convertToCents(Double price) {
        return price * 100;
    }

    @Transactional
    public ProductData updateProduct(Long id, ProductUpdateData data){

        Product product = productRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Product not found")
        );

        if(data.name() != null) product.setName(data.name());
        if(data.images() != null) {
            ArrayList<String> images = new ArrayList<>(product.getImages());
            images.addAll(data.images());
            product.setImages(images);
        }
        if(data.stock() != null) product.setStock(data.stock());
        if(data.price() != null) product.setPrice(convertToCents(data.price()));
        if(data.description() != null) product.setDescription(data.description());
        if(data.categories() != null) addCategories(product, data.categories());


        productRepository.save(product);
        return new ProductData(product);
    }

//    Temporarily delete, future will set product as inactive and will be deleted few days later, removing all references.
    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Product not found")
        );
        productRepository.delete(product);
    }

    @Transactional
    public void addCategories(Product product, HashSet<Long> categoriesIds){
        var categories = product.getCategoriesData();
        for(Long id : categoriesIds){
            Category category = categoryRepository.findById(id).orElseThrow(
                    () -> new BadRequestException("Category not found")
            );
            if(!category.getIsActive()) throw new BadRequestException(category.getName() + " is inactive");
            if(categories.contains(category)) continue;
            product.addCategory(category);
        }
    }




}
