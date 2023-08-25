package io.github.kleberrhuan.butcherapp.domain.services;

import io.github.kleberrhuan.butcherapp.domain.entities.ResetPasswordCode;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import io.github.kleberrhuan.butcherapp.domain.records.auth.AuthenticationResponse;
import io.github.kleberrhuan.butcherapp.domain.records.user.LoginData;
import io.github.kleberrhuan.butcherapp.domain.records.user.PwResetData;
import io.github.kleberrhuan.butcherapp.domain.records.user.RegisterData;
import io.github.kleberrhuan.butcherapp.domain.repositories.ResetPasswordRepository;
import io.github.kleberrhuan.butcherapp.domain.repositories.UserRepository;
import io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors.BadRequestException;
import io.github.kleberrhuan.butcherapp.infra.config.mail.EmailService;
import io.github.kleberrhuan.butcherapp.infra.security.jwt.JwtServices;
import io.github.kleberrhuan.butcherapp.infra.security.jwt.jwtDTO.CreateTokenData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServices {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtServices jwtTokenServices;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ResetPasswordRepository resetPasswordRepository;
    private final CartServices cartServices;

    @Transactional
    public User register(RegisterData data) {
        var user = User.builder()
                .firstName(data.firstName())
                .lastName(data.lastName())
                .email(data.getEmail())
                .password(encoder.encode(data.password()))
                .verificationCode(generateUUID())
                .build();
        var jwtToken = jwtTokenServices.generateToken(user);
        userRepository.save(user);
        cartServices.createCart(user);
        return user;
    }

    public AuthenticationResponse login(LoginData data) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.password())
            );
            var user = userRepository.findByEmail(data.getEmail())
                    .orElseThrow();

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", user.getId());

            var tokenData = new CreateTokenData(extraClaims, user);
            var jwtToken = jwtTokenServices.generateToken(tokenData);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            if (e.getMessage().contains("Bad credentials")) throw new BadRequestException("Invalid credentials");
            else throw new InternalError("Error while authenticating user" + e.getMessage());
        }
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Transactional
    public void verifyUser(String verificationCode) {
        var user = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new BadRequestException("User not found"));
        user.setVerified();
        userRepository.save(user);
    }

    public void generateResetPasswordCode(User user) {
        ResetPasswordCode resetPasswordCode = ResetPasswordCode.builder()
                .code(generateUUID())
                .user(user)
                .isUsed(false)
                .build();
        user.setResetPasswordCode(resetPasswordCode);
    }

    @Transactional
    public void setNewPassword(PwResetData data) {
        User user = userRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        ResetPasswordCode resetPasswordCode = user.getResetPasswordCode();

        if(!Objects.equals(data.code(), resetPasswordCode.getCode())) throw new BadRequestException("Invalid code");
        if (resetPasswordCode.getIsUsed()) throw new BadRequestException("Code already used");
        if (resetPasswordCode.isExpired()) throw new BadRequestException("Code expired");

        user.setPassword(encoder.encode(data.password()));
        resetPasswordCode.setIsUsed(true);

        userRepository.save(user);
    }

    @Transactional
    public void sendResetPasswordCode(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));
        ResetPasswordCode resetPasswordCode = user.getResetPasswordCode();
        if(resetPasswordCode == null || resetPasswordCode.isExpired() || resetPasswordCode.isUsed()) {
            if (resetPasswordCode != null) removeResetPasswordCode(userEmail);
            this.generateResetPasswordCode(user);
            userRepository.save(user);
        }
    }

    @Transactional
    public void removeResetPasswordCode(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));
        ResetPasswordCode resetPasswordCode = user.getResetPasswordCode();
        if(resetPasswordCode != null) {
            user.setResetPasswordCode(null);
            userRepository.save(user);
            resetPasswordRepository.delete(resetPasswordCode);
        }
    }

    public boolean verifyResetPasswordCode(String code, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BadRequestException("User not found"));
        return user.getResetCode().equals(code);
    }

}
