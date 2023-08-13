package io.github.kleberrhuan.butcherapp.controllers;

import io.github.kleberrhuan.butcherapp.domain.entities.Product;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductCreateData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductData;
import io.github.kleberrhuan.butcherapp.domain.records.product.ProductUpdateData;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.domain.services.ProductServices;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.ForbiddenException;
import io.github.kleberrhuan.butcherapp.infra.security.jwt.JwtServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static io.github.kleberrhuan.butcherapp.infra.config.ApplicationConfig.prefixPath;


@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final ProductServices productServices;
    private final JwtServices jwtServices;

    @PostMapping("/products/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductCreateData data,
                                           @RequestHeader("Authorization") String token,
                                           UriComponentsBuilder uri) {
        String userEmail = jwtServices.getUsernameFromHeader(token);
        var product = productServices.createProduct(data, userEmail);
        URI location = uri.path(prefixPath + "/events/id/{id}")
                .buildAndExpand(product.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<ProductData> updateProduct(@PathVariable Long id,
                                           @RequestBody @Valid ProductUpdateData data) {
        ProductData product = productServices.updateProduct(id, data);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productServices.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
