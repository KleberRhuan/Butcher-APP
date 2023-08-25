package io.github.kleberrhuan.butcherapp.domain.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.kleberrhuan.butcherapp.domain.entities.Cart.CartProduct;
import io.github.kleberrhuan.butcherapp.domain.records.category.CategoryData;
import io.github.kleberrhuan.butcherapp.domain.records.category.ProductCategoryData;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "products_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id @GeneratedValue
    private Long id;
    private String name;
    @ElementCollection
    @Column(columnDefinition = "text[]")
    private List<String> images = new ArrayList<>();
    private Integer stock;
    private Double price;
    @JoinColumn(name="created_by")
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;
    private String description;
    @JoinTable(name = "products_table_categories",
            joinColumns = @JoinColumn(name = "products_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id"))
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Category> categories = new HashSet<>();
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;
    @Column(name = "is_active", insertable = false, columnDefinition = "boolean default true")
    private Boolean isActive;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartProduct> cartItems;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public Set<ProductCategoryData> getCategories() {
        Set<ProductCategoryData> categoriesData = new HashSet<>();
        for (Category category : categories) {
            categoriesData.add(new ProductCategoryData(category));
        }
        return categoriesData;
    }
}
