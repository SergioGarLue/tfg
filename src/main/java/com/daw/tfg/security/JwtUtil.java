package com.daw.tfg.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    public SecretKey getSigningKey() {
        // Asegúrate de que tu secretKeyString tenga al menos 32 caracteres
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Extraemos los roles (Authorities) para meterlos en el JWT
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        claims.put("roles", roles); 
        
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        // El refresh token normalmente no necesita roles, solo el sujeto para renovar
        return createToken(new HashMap<>(), userDetails.getUsername(), refreshTokenExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Nuevo método: Útil si quieres verificar los roles sin ir a la base de datos
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> (List<String>) claims.get("roles"));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}