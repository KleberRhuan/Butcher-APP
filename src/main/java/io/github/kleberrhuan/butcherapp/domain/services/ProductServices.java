package io.github.kleberrhuan.butcherapp.domain.services;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductCreateData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductUpdateData;
import io.github.kleberrhuan.butcherapp.domain.repositories.ProductRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.ForbiddenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServices {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

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

    public Double convertToCents(Double price) {
        return price * 100;
    }

    @Transactional
    public ProductData updateProduct(Long id, ProductUpdateData data){

        Product product = productRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Product not found")
        );

        if(data.name() != null) product.setName(data.name());
        if(data.images() != null) product.setImages(new ArrayList<>(data.images()));
        if(data.stock() != null) product.setStock(data.stock());
        if(data.price() != null) product.setPrice(convertToCents(data.price()));
        if(data.description() != null) product.setDescription(data.description());

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




}
