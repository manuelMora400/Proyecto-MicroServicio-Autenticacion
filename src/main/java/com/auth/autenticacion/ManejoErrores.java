package com.auth.autenticacion;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.autenticacion.dto.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
// Para capturar erroress globales de este backend ademas evitar try catch
// controller, para no sobrecargar el controlador

public class ManejoErrores {

    // Cuando falla el @Valid en un request
    @ExceptionHandler(MethodArgumentNotValidException.class)

    // Se ejecuta automaticamente cuando ocurre una exception de la validacacion
    public ResponseEntity<ErrorDTO> manejarErroresValidacion(

            MethodArgumentNotValidException ex, // Detalles de los errore de validicion
            HttpServletRequest request) { // informacion del request URL

        // Mapa donde se almacenaran los errores
        Map<String, String> errores = new HashMap<>();

        // Se recorre todos los errores de validacion detectado por el spring
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                400,
                "Error de validacion",
                errores, // Detalle de error del campo
                request.getRequestURI()); // Url donde ocurrio el error
        return ResponseEntity.badRequest().body(errorDTO);
    }
}
