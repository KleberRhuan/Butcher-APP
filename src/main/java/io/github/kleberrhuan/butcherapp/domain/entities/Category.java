package io.github.kleberrhuan.butcherapp.domain.entities;

import io.github.kleberrhuan.butcherapp.domain.enums.category.CategoryOrder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories_table")
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String image;
    private String description;
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new LinkedHashSet<>();
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_order", nullable = false, columnDefinition = "smallint")
    private CategoryOrder order;
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Category parent;
    @JoinColumn(name = "created_by")
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;
    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at",updatable = false)
    private LocalDateTime updatedAt;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
