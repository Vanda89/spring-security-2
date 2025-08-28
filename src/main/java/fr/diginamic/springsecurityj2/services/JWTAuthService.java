package fr.diginamic.springsecurityj2.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service for generating JSON Web Tokens (JWT) using HS256.
 *
 * This service provides a method to create a signed JWT token for a username.
 */
@Service
public class JWTAuthService {
    @Getter
    private String lastToken;

    /**
     * Retrieves the secret key used to sign JWT tokens.
     *
     * @return a {@link SecretKey} used for HMAC signing of JWTs
     */
    private SecretKey getSigningKey() {
        String secretString = "maSuperCleSecrete123maSuperCleSecrete123";
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token containing a message as a claim.
     *
     * @param message the message to include in the JWT payload
     * @return a signed JWT {@link String} containing the message
     */
    public String generateToken(String message) {
        SecretKey key = getSigningKey();
        String jwt = Jwts.builder()
                .setSubject(message)
                .claim("message", message)
                .claim("role", "USER")
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
        this.lastToken = jwt;
        return jwt;
    }

    /**
     * Validates a JWT token and extracts its claims.
     *
     * @param token the JWT string to validate
     * @return the {@link Claims} contained in the token if it is valid
     * @throws io.jsonwebtoken.JwtException if the token is invalid, expired, or cannot be parsed
     */
    public Claims validateToken(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}