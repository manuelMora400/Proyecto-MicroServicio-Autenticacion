package com.auth.autenticacion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.autenticacion.dto.BuscarDatosSegurosDTO;
import com.auth.autenticacion.dto.LoginSeguroDTO;
import com.auth.autenticacion.dto.UsuarioSeguroDTO;
import com.auth.autenticacion.model.Usuario;
import com.auth.autenticacion.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /*
     * Metodo (get) listar todas las cuentas de usuarios,
     * si el administrador lo requiere, puede retornar en el historial
     */
    public List<Usuario> listar() {
        return repository.findAll();
    }

    // Get : metodo buscar si existe (nombre usuario)
    public Optional<Usuario> buscarPorNombre(String nombre) {

        // Valida que no sea null, que no este en vacio ni que contenga espacios
        if (nombre == null || nombre.isBlank()) {
            return Optional.empty();
        }
        return repository.findByNombreUsuario(nombre);
    }

    // Get : buscar si existe email (Aun sin validaciones)
    public Optional<Usuario> buscarEmail(String email) {

        // Valida que no sea null, que no este en vacio ni que contenga espacios
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return repository.findByEmail(email);
    }

    // Get buscar por id
    public Optional<Usuario> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty(); // si el Id null, Optional va retornar algo vacio.
        }
        return repository.findById(id);
    }

    // Post: Metodo crear cuenta usuario(aun sin validaciones)
    public Optional<Usuario> crear(Usuario usuario) {

        // Validamos que los datos ingresados no sea null y que no tenga espacios.
        if (usuario == null) {
            return Optional.empty();
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank()) {
            return Optional.empty();
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            return Optional.empty();
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().isBlank()) {
            return Optional.empty();
        }

        Optional<Usuario> nombre = repository.findByNombreUsuario(usuario.getNombreUsuario());
        Optional<Usuario> email = repository.findByEmail(usuario.getEmail());

        if (nombre.isPresent() || email.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(repository.save(usuario));
    }

    // Post : Login
    /*
     * validamos que nombreusuario y contraseña no tengam espacios ni que este
     * vacio y que no sea nulo
     * ademas se valida que:
     * si la contraseña de la bd sea igual que del usuario que ingrese, si es cierto
     * retorna que existe y es valido
     */

    public Optional<Usuario> login(String nombreUsuario, String contraseña) {

        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            return Optional.empty();
        }
        if (contraseña == null || contraseña.isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> existe = repository.findByNombreUsuario(nombreUsuario);
        if (existe.isEmpty()) {
            return Optional.empty();
        }
        Usuario usuario = existe.get();

        // por ahora validamos asi la contraseña porque aun no tenemos conocimiento de
        // security
        if (contraseña.equals(usuario.getContraseña())) {
            return existe;
        }
        return Optional.empty();
    }

    // Metodo put: Actualizar por id -> permitira actualizar todo lo que se requiera
    public Optional<Usuario> actualizarPorId(Integer id, Usuario usuario) {
        // validamos que el usuario no sea nul recien alli ingresamos los get
        if (usuario == null) {
            return Optional.empty();
        }

        // basta que uno de los get o id venga nulo para avisar el error y que no tenga
        // espacios en blancos
        if (id == null || usuario.getNombreUsuario() == null || usuario.getContraseña() == null
                || usuario.getEmail() == null || usuario.getRol() == null
                || usuario.getNombreUsuario().isBlank() || usuario.getContraseña().isBlank()
                || usuario.getEmail().isBlank() || usuario.getRol().isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> existe = repository.findById(id);
        if (existe.isPresent()) {
            Usuario usuarios = existe.get();
            usuarios.setNombreUsuario(usuario.getNombreUsuario());
            usuarios.setContraseña(usuario.getContraseña());
            usuarios.setEmail(usuario.getEmail());
            usuarios.setRol(usuario.getRol());
            return Optional.of(repository.save(usuarios)); // Retornamos el valor que queremos actualizar
        }
        return Optional.empty();
    }

    // Delete: Metodo eliminar usuario
    public boolean eliminar(Integer id) {
        if (id == null) {
            return false;
        }
        if (repository.existsById(id)) { // pregunta ¿existe el id? si existe lo elimina
            repository.deleteById(id);
            return true; // deleteBtId -> es un void
        }
        return false;
    }

    // -------------------- DTO uso exclusivos --------------------------------
    // DTO listar solo datos No sensibles del usuario
    public List<UsuarioSeguroDTO> usuariosSeguros() {
        List<Usuario> usuarios = repository.findAll(); // llamo todo lo de usuario en la base de datos
        List<UsuarioSeguroDTO> lista = new ArrayList<>(); // se crea una lista vacia que contrenda dto

        for (Usuario u : usuarios) { // se recorre los usuarios

            UsuarioSeguroDTO dto = new UsuarioSeguroDTO();

            dto.setNombreUsuario(u.getNombreUsuario());
            dto.setEmail(u.getEmail());
            // Email y nombre solo se copian esos datos para los dto.
            lista.add(dto);

        }
        return lista; // Se retorna la lista con los dto listos
    }

    // DTO login seguro se evita filtrar la contraseña.

    public Optional<LoginSeguroDTO> loginSeguro(String nombreUsuario, String password) {

        if (nombreUsuario == null || nombreUsuario.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        Optional<Usuario> usuario = repository.findByNombreUsuario(nombreUsuario);
        LoginSeguroDTO dto = new LoginSeguroDTO();
        // Antes de llamar el usuario con todo sus atributos verificamos que venga vacio
        if (usuario.isEmpty()) {
            return Optional.empty();
        }
        Usuario usuarios = usuario.get();

        if (password.equals(usuarios.getContraseña())) {
            dto.setNombreUsuario(usuarios.getNombreUsuario());
            dto.setEmail(usuarios.getEmail());
            dto.setRol(usuarios.getRol());

            return Optional.of(dto); // dice si sale todo ok? entra al return

        }
        return Optional.empty(); // si no retornara vacio.
    }

    /*
     * hago un Metodo para reutilizar codigo porque DTO de los 3 buscar
     * nombre,email y id se repetia logica.
     */
    public BuscarDatosSegurosDTO buscarDatos(Usuario usuario) {
        BuscarDatosSegurosDTO dto = new BuscarDatosSegurosDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }

    // DTO para buscar nombreUsuario.
    public Optional<BuscarDatosSegurosDTO> buscarNombreDTO(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> users = repository.findByNombreUsuario(nombre);

        if (users.isEmpty()) {
            return Optional.empty();
        }
        // Si el usuario existe y se buscara en la base de datoss y se retorna por el
        // .get()
        // Traera todos los datos necesarios del DTO
        return Optional.of(buscarDatos(users.get()));

        /*
         * Al utilizar el metodo buscarDatos estamos reutilizando codigo
         * se utilizara para los otros 2 metodos buscar id y email, porque se repite
         * logica
         */
    }

    // DTO para buscar email.
    public Optional<BuscarDatosSegurosDTO> buscarEmailDTO(String email) {

        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> users = repository.findByEmail(email);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(buscarDatos(users.get()));
    }

    // DTO para buscar id.
    public Optional<BuscarDatosSegurosDTO> buscarIdDTO(Integer id) {

        if (id == null) {
            return Optional.empty();
        }
        Optional<Usuario> users = repository.findById(id);

        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(buscarDatos(users.get()));
    }

}
