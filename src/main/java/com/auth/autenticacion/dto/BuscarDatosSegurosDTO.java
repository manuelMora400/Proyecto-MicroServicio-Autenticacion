package com.auth.autenticacion.dto;

import lombok.Data;

@Data
public class BuscarDatosSegurosDTO {
    private Integer id;
    private String nombreUsuario;
    private String email;
    private String rol;
}
