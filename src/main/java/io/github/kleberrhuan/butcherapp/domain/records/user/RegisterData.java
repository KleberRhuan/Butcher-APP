package io.github.kleberrhuan.butcherapp.domain.records.user;

import io.github.kleberrhuan.butcherapp.infra.config.validators.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record RegisterData(
        @NotBlank(message = "First Name {default.required}")
        String firstName,
        @NotBlank(message = "Last name {default.required}")
        String lastName,
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.invalid}")
        String email,
        @ValidPassword
        @NotBlank(message = "{password.required}")
        String password
) {
        public String getEmail(){
                return email.toLowerCase();
        }
}
