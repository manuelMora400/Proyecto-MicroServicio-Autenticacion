package com.auth.autenticacion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired

    private UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    //Get listar todo
    @GetMapping()
    public List<Usuario> listarTodos() {
        return service.listar();
    }
    

    // Metodo get con dto solo mostrar datos no sensibles del usuario.
    @GetMapping("/listar-dto")
    public ResponseEntity<?> listarUsuariosSeguros() {
        return ResponseEntity.ok(service.usuariosSeguros()); // Si sale biem retorna un 200
    }

    // Metodo Get buscar por nombre con DTO para proteger los datos sensibles
    @GetMapping("/buscar/nombre/{nombreUsuario}")
    public ResponseEntity<?> buscarNombreUsuario(@PathVariable String nombreUsuario) {
        try {

            Optional<BuscarDatosSegurosDTO> existe = service.buscarNombreDTO(nombreUsuario);
            if (existe.isPresent()) {
                return ResponseEntity.ok(existe.get());

            }
            return ResponseEntity.status(404).body("El nombre no existe");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error del sistema");
        }

    }

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

    // Metodo buscar por id con DTO para proteger los datos sensibles
    @GetMapping("/buscar/usuario/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<BuscarDatosSegurosDTO> existe = service.buscarIdDTO(id);
        try {
            if (existe.isEmpty()) {
                return ResponseEntity.status(404).body("El Id no existe");
            }
            return ResponseEntity.ok(existe.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de la base de datos");
        }

    }

    // metodo post crear una cuenta de usuario.
    @PostMapping("/agregar")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult resultado) {

        if (resultado.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            resultado.getFieldErrors().forEach(errores -> error.put(errores.getField(), errores.getDefaultMessage()));
            return ResponseEntity.badRequest().body(error);
        }
        try {
            boolean mensaje = service.crear(usuario);

            if (mensaje) {
                return ResponseEntity.status(201).body("Usuario creado con exito");
            }
            return ResponseEntity.status(400).body("Usuario existe o daros invalidos");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error del sistema");
        }

    }

    // metodo post login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(errores -> error.put(errores.getField(), errores.getDefaultMessage()));
            return ResponseEntity.badRequest().body(error);
        }
        try {
            Optional<LoginSeguroDTO> login = service.loginSeguro(usuario.getNombreUsuario(), usuario.getContraseña());
            if (login.isPresent()) {
                return ResponseEntity.ok(login.get());
            }
            return ResponseEntity.status(404).body("Error datos invalidos");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de la base de datos");
        }
    }

    // Metodo Put: Actualizar por id
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarId(@PathVariable Integer id, @Valid @RequestBody Usuario usuario,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(errores -> error.put(errores.getField(), errores.getDefaultMessage()));
            return ResponseEntity.badRequest().body(error);
        }
        try {
            Optional<Usuario> actualizar = service.actualizarPorId(id, usuario);
            if (actualizar.isEmpty()) {
                return ResponseEntity.status(400).body("No se pudo actualizar datos erroneos");
            }
            return ResponseEntity.ok(actualizar.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error de la base de datos");
        }
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
