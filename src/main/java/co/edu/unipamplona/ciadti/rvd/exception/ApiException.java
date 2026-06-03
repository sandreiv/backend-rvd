/**
 * Aplicación: rvd
 * Archivo: ApiException.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.email
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 08/04/2026
 * Modificaciones:
 * 08/04/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() { return status; }
}

/* 08/04/2026 @:Sebastian Jaimes */