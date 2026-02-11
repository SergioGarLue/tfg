package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.PerfilUsuarioRepository;
import com.daw.tfg.models.PerfilUsuario;

@Service
public class PerfilUsuarioService {

    private final PerfilUsuarioRepository perfilRepository;

    public PerfilUsuarioService(PerfilUsuarioRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public List<PerfilUsuario> findAll() {
        return perfilRepository.findAll();
    }

    public Optional<PerfilUsuario> findById(Long id) {
        return perfilRepository.findById(id);
    }

    public PerfilUsuario save(PerfilUsuario perfil) {
        return perfilRepository.save(perfil);
    }

    public void deleteById(Long id) {
        perfilRepository.deleteById(id);
    }
}

