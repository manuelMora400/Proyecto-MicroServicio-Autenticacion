package com.auth.autenticacion.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDTO {

    private LocalDateTime timestamp; // fecha y hora de hoy en que ocurrio el error
    private int status; // Codigo de estado HTTP : 200,201,400 etc.
    private String mensaje; // Mensaje general de error
    private Map<String, String> errores; // dar detalles al error
    private String path; // Ruta del edpoints donde ocurrio el error.

    // Contructor
    public ErrorDTO(LocalDateTime timestamp, int status, String mensaje, Map<String, String> errores, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.mensaje = mensaje;
        this.errores = errores;
        this.path = path;
    }
}
