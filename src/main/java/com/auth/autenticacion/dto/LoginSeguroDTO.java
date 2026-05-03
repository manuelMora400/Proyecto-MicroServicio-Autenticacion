package com.auth.autenticacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginSeguroDTO {

    @NotBlank(message = "No puede tener nombre de usuario vacio")
    private String nombreUsuario;

    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "El Correo electronico no puede estar vacio")
    private String email;

    @NotBlank(message = "El rol del usuario no puede estar vacio")
    private String rol;

}
