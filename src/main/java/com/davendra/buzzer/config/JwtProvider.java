package com.davendra.buzzer.config;

import com.davendra.buzzer.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

// for jwt creation
public class JwtProvider {
    private static SecretKey secretKey = Keys.hmacShaKeyFor(Constants.SECRET_KEY.getBytes());

    public static String generateToken(Authentication auth) {
        Instant expirationTime = Instant.now().plusSeconds(Constants.JWT_EXPIRATION);

        return Jwts.builder()
                .setIssuer("Davendra")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expirationTime))
                .claim("email", auth.getPrincipal())
                .signWith(secretKey)
                .compact();
    }

    public static String getEmailFromJwt(String jwt) {
        jwt = jwt.substring(7); // skips Bearer with space
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody(); // to eextract email setted in claim

        String email = String.valueOf(claims.get("email"));
        return email;
    }
}
