package io.github.kleberrhuan.butcherapp.infra.security.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.kleberrhuan.butcherapp.infra.security.jwt.jwtDTO.CreateTokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServices {

    private final Dotenv dotenv;

    public String generateToken(UserDetails userDetails){
        return generateToken(new CreateTokenData(Map.of(), userDetails));
    }

    public String generateToken(CreateTokenData data){

       var extraClaims = data.extraClaims();
       var userDetails = data.userDetails();

       return Jwts
               .builder()
               .setClaims(extraClaims)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(data.getIssuedAt())
               .setExpiration(data.getExpiration())
               .signWith(secretKey())
               .compact();
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails){
         final String username = getUsername(jwtToken);
         return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    public boolean isTokenExpired(String jwtToken) {
        return getClaims(jwtToken)
                .getExpiration()
                .before(new Date());
    }

    public String getUsername(String jwtToken) {
        return getClaims(jwtToken)
                .getSubject();
    }

    public String getUsernameFromHeader(String authorizationHeader){
        var tokenJwt = authorizationHeader
                .replace("Bearer ", "");
        return getUsername(tokenJwt);
    }

   public Claims getClaims(String jwtToken){
       return Jwts
               .parserBuilder()
               .setSigningKey(secretKey())
               .build()
               .parseClaimsJws(jwtToken)
               .getBody();
   }

   public <T> T getClaim(String jwtToken, Function<Claims, T> claimsResolver) {
       return claimsResolver.apply(getClaims(jwtToken));
   }

    private Key secretKey() {
        return Keys.hmacShaKeyFor(
                Objects
                        .requireNonNull(dotenv.get("JWT_SECRET"))
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

}
