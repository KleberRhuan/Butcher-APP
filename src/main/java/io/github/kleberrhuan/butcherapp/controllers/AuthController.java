package io.github.kleberrhuan.butcherapp.controllers;


import io.github.kleberrhuan.butcherapp.domain.records.auth.AuthenticationResponse;
import io.github.kleberrhuan.butcherapp.domain.records.user.LoginData;
import io.github.kleberrhuan.butcherapp.domain.records.user.PwResetData;
import io.github.kleberrhuan.butcherapp.domain.records.user.RegisterData;
import io.github.kleberrhuan.butcherapp.domain.services.AuthenticationServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.github.kleberrhuan.butcherapp.infra.config.ApplicationConfig.prefixPath;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationServices services;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterData data,
                                                           UriComponentsBuilder uriBuilder) {
        var user = services.register(data);

        URI location = uriBuilder.path(prefixPath + "/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid LoginData data) {
        return ResponseEntity.ok(services.login(data));
    }

    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<?> verifyUser(@PathVariable String verificationCode) {
        services.verifyUser(verificationCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        services.sendResetPasswordCode(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset-password/{resetPasswordCode}")
    public ResponseEntity<?> checkCode(@PathVariable String resetPasswordCode, @RequestParam String email) {

        if(email.isEmpty()) return ResponseEntity.badRequest().body("Email is required");

        var isValid = services.verifyResetPasswordCode(resetPasswordCode, email);
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Invalid code");
        if (!isValid) return ResponseEntity.badRequest().body(message);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PwResetData data) {
        services.setNewPassword(data);
        return ResponseEntity.ok().build();
    }

}
