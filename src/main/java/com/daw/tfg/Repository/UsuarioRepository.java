package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario); // para encontrar usuarios y poder agregarlos, tmb para login y reseñas.
    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreParte); // para buscar nombres parecidos
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    // Usuario save(Usuario usuario); para guardar un nuevo usuario
    // void deleteById(Long id);  para eliminar un usuario por ID
    // Usuario update(Usuario usuario);  para actualizar la información de un usuario
    // List<Usuario> findAll();  para obtener todos los usuarios
    boolean existsByNombreUsuario(String nombreUsuario); // para verificar si un usuario existe por nombre de usuario
    boolean existsByEmail(String email); // para verificar si un usuario existe por email
}
