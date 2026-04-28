package com.daw.tfg.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.tfg.repository.UsuarioRepository;
import com.daw.tfg.models.Usuario;

@Service
public class CustomUserDetails implements UserDetailsService{

    private UsuarioRepository usuarioRepository;

    public CustomUserDetails(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado" + username));
        
        if (usuario.getDesarrollador() != null) {
            usuario.getDesarrollador().getNombre();
        }
        
        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getContraseñaCifrada())
                .authorities("ROLE_"+usuario.getRol().name())
                .build();
        
    }

}
