package com.auth.autenticacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.autenticacion.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo (post) crear una cuenta del usuario ya esta en Jpa -> service
    // Metodo (Get) listar todas las cuentas esta en Jpa -> service
    /*
     * Metodo (Put) actualizar nombreUsuario, contraseña y gmail -- save() -> Listo
     * por Jpa
     */

    // metodo get buscar nombre
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    // Metodo get buscar gmail
    Optional<Usuario> findByEmail(String email);

}