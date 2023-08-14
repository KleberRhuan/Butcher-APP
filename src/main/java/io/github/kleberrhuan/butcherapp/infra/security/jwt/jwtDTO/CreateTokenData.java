package io.github.kleberrhuan.butcherapp.infra.security.jwt.jwtDTO;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public record CreateTokenData(
        Map<String, Object> extraClaims,
        UserDetails userDetails
) {
    public Date getIssuedAt() {
        return new Date(System.currentTimeMillis());
    }

    public Date getExpiration() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
    }

}
