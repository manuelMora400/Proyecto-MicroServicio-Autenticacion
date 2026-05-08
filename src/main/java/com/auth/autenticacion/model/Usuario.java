package com.auth.autenticacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    // defino que el id sea autoincrementable en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "No puede tener nombre de usuario vacio")
    @Column(nullable = false, length = 25, unique = true) // La base de datos tendra un limite de 25 caracteres y nombre unico
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(min = 8, max = 12, message = "La contraseña debe tener al menos 8 digitos y maximo 12 digitos")
    @Column(nullable = false)
    private String password;

    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "El Correo electronico no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El rol del usuario no puede estar vacio")
    @Column(nullable = false, length = 50)
    private String rol;

}
