package io.github.kleberrhuan.butcherapp.domain.records.user;

import io.github.kleberrhuan.butcherapp.infra.config.validators.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PwResetData(
        @Email(message = "{email.invalid}")
        @NotBlank(message = "{email.required}")
        String email,
        @NotBlank(message = "{code.required}")
        String code,
        @NotBlank(message = "{password.required}")
        @ValidPassword
        String password
) {
        public String getEmail(){
                return email.toLowerCase();
        }
}
