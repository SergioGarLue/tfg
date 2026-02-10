package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.Perfil_UsuarioRepository;
import com.daw.tfg.models.Perfil_Usuario;

@Service
public class Perfil_UsuarioService {

    private final Perfil_UsuarioRepository perfilRepository;

    public Perfil_UsuarioService(Perfil_UsuarioRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public List<Perfil_Usuario> findAll() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil_Usuario> findById(Long id) {
        return perfilRepository.findById(id);
    }

    public Perfil_Usuario save(Perfil_Usuario perfil) {
        return perfilRepository.save(perfil);
    }

    public void deleteById(Long id) {
        perfilRepository.deleteById(id);
    }
}

