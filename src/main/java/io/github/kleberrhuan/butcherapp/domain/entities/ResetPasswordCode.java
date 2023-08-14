package io.github.kleberrhuan.butcherapp.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reset_password_codes")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordCode {

    @Id
    @GeneratedValue
    private Long id;
    private String code;
    @Column(name = "is_used", insertable = false)
    private Boolean isUsed;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isExpired() {
        return createdAt.plusMinutes(60).isBefore(LocalDateTime.now());
    }

    public boolean isUsed() {
        return isUsed;
    }
}
