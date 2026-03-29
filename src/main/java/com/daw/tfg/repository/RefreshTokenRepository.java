package com.daw.tfg.repository;

import com.daw.tfg.models.RefreshToken;
import com.daw.tfg.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUser(Usuario user);
    void deleteByUser(Usuario user);
}
