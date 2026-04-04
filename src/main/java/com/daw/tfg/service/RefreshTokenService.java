package com.daw.tfg.service;

import com.daw.tfg.models.RefreshToken;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.RefreshTokenRepository;
import com.daw.tfg.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    /**
     * Servicio para gestión de refresh tokens JWT.
     * Maneja creación, validación y revocación de tokens.
     * 
     */

    // Repositorios para operaciones de BD
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     */
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UsuarioRepository usuarioRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Value("${jwt.refresh.expiration:604800000}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        revokeAllRefreshTokensByUserId(userId);

        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String tokenString = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(tokenString);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            revokeRefreshToken(token);
            throw new IllegalArgumentException("Refresh token expired");
        }
        return token;
    }

    @Transactional
    public void revokeRefreshToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllRefreshTokensByUserId(Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshToken> findActiveByToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = findByToken(token);
        return refreshTokenOpt.filter(refreshToken -> !refreshToken.isRevoked());
    }
}
