package io.github.kleberrhuan.butcherapp.domain.records.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginData(
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.invalid}")
        String email,
        @NotBlank(message = "{password.required}")
        String password
) {
        public String getEmail(){
                return email.toLowerCase();
        }
}
