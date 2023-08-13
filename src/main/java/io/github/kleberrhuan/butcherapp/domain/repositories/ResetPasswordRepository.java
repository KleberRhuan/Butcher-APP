package io.github.kleberrhuan.butcherapp.domain.repositories;

import io.github.kleberrhuan.butcherapp.domain.entities.ResetPasswordCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordCode, Long> {
}
