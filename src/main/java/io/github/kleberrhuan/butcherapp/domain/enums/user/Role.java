package io.github.kleberrhuan.butcherapp.domain.enums.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

}
