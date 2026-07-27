package com.formcrafter.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String username) {
        return generateToken(username, jwtProperties.accessTokenExpiration(), TokenType.ACCESS);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtProperties.refreshTokenExpiration(), TokenType.REFRESH);
    }

    public Duration getAccessTokenExpiration() {
        return jwtProperties.accessTokenExpiration();
    }

    public Duration getRefreshTokenExpiration() {
        return jwtProperties.refreshTokenExpiration();
    }

    public boolean isTokenValid(String token, String expectedUsername, TokenType expectedType) {
        try {
            Claims claims = extractAllClaims(token);

            String actualUsername = claims.getSubject();
            String actualType = (String) claims.get("type");
            Date expiration = claims.getExpiration();

            return actualUsername.equals(expectedUsername)
                    && expectedType.name().equals(actualType)
                    && expiration.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private String generateToken(String username, Duration expiration, TokenType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type.name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
