package com.daw.tfg.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.UsuarioRepository;
import com.daw.tfg.models.Usuario;

@Service
public class CustomUserDetails implements UserDetailsService{

    private UsuarioRepository usuarioRepository;

    public CustomUserDetails(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByName(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado" + username));
        return User.builder()
                .username(usuario.getNombre_usuario())
                .password(usuario.getContraseña_cifrada())
                
                .build();
        
    }

}
