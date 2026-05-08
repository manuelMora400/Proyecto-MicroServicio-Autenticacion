package com.auth.autenticacion.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.autenticacion.dto.BuscarDatosSegurosDTO;
import com.auth.autenticacion.dto.LoginSeguroDTO;
import com.auth.autenticacion.model.Usuario;
import com.auth.autenticacion.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired

    private UsuarioService service;

    // Get listar todo
    @GetMapping()
    public List<Usuario> listarTodos() {
        return service.listar();
    }

    // Metodo get con dto solo mostrar datos no sensibles del usuario.
    @GetMapping("/listar-dto")
    public ResponseEntity<?> listarUsuariosSeguros() {
        return ResponseEntity.ok(service.usuariosSeguros()); // Si sale biem retorna un 200
    }

    /*
     * solo hacemos get : buscar por email
     * por que uno -> evitamos sobrecargar el contoller y
     * dos -> es mas realista buscar por email, ademas buscar por nombre y id lo
     * usamos
     * de mejor forma en el service al momento de crear y login.
     */

    // Get buscar email del usuario con DTO para proteger los datos sensibles
    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<?> buscarEmail(@PathVariable String email) {
        try {
            Optional<BuscarDatosSegurosDTO> existe = service.buscarEmailDTO(email);

            if (existe.isEmpty()) {
                return ResponseEntity.status(404).body("El email no existe");
            }
            return ResponseEntity.ok(existe.get()); // devuelve el objeto Usuario en este caso el Email

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error del sistema");

        }
    }

    // metodo post crear una cuenta de usuario.
    @PostMapping("/agregar")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevo = service.crear(usuario);
        return ResponseEntity.status(201).body(nuevo);
    }

    // metodo post login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Usuario usuario) {
        Optional<LoginSeguroDTO> login = service.loginSeguro(usuario.getNombreUsuario(), usuario.getPassword());
        return ResponseEntity.ok(login);
    }

    // Metodo Put: Actualizar por id
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarId(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        Optional<Usuario> actualizar = service.actualizarPorId(id, usuario);
        return ResponseEntity.status(200).body(actualizar);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuarios(@PathVariable Integer id) {

        try {
            boolean existe = service.eliminar(id);
            if (existe) {
                return ResponseEntity.ok().body("Eliminado con exito");
            }
            return ResponseEntity.status(404).body("Error al eliminar, intente de nuevo");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de la base de datos");

        }
    }

}
