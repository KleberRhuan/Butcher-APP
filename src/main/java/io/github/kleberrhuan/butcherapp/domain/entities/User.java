package io.github.kleberrhuan.butcherapp.domain.entities;

import io.github.kleberrhuan.butcherapp.domain.entities.Cart.Cart;
import io.github.kleberrhuan.butcherapp.domain.enums.user.Role;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "users_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    @Column(name = "is_verified", insertable = false)
    private Boolean isVerified;
    @Column(name = "verification_code")
    private String verificationCode;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @Nullable
    private ResetPasswordCode resetPasswordCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false)
    private Role role;
    @Column(name = "is_inactive", insertable = false, columnDefinition = "boolean default false")
    private Boolean isInactive;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isInactive;
    }
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getResetCode() {
        if (resetPasswordCode == null) return null;
        return resetPasswordCode.getCode();
    }

    public void setVerified() {
        if (isVerified) throw new BadRequestException("User already verified");
        this.isVerified = true;
    }
}
