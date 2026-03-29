package com.daw.tfg.controllers;

import com.daw.tfg.dtos.AuthResponse;
import com.daw.tfg.dtos.LoginRequest;
import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.models.RefreshToken;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.security.JwtUtil;
import com.daw.tfg.service.CustomUserDetails;
import com.daw.tfg.service.RefreshTokenService;
import com.daw.tfg.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetails userDetailsService;

    public AuthController(UsuarioService usuarioService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil, CustomUserDetails userDetailsService) {
        this.usuarioService = usuarioService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var userOpt = usuarioService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Usuario user = userOpt.get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getNombreUsuario());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getIdUsuario());
        String refreshTokenStr = refreshToken.getToken();

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshTokenStr, user.getNombreUsuario()));
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UsuarioDTO userDto) {
        usuarioService.registrar(userDto);
        Usuario user = usuarioService.findByNombreUsuario(userDto.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getNombreUsuario());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getIdUsuario());
        String refreshTokenStr = refreshToken.getToken();

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshTokenStr, user.getNombreUsuario()));
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.findActiveByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        Usuario user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getNombreUsuario());

        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshTokenStr, user.getNombreUsuario()));
    }

    @PostMapping("/logout")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> logout(@RequestBody String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        refreshTokenService.revokeRefreshToken(refreshToken);
        refreshTokenService.revokeAllRefreshTokensByUserId(refreshToken.getUser().getIdUsuario());
        return ResponseEntity.ok().build();
    }
}
