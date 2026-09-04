/**
 * Aplicación: rvd
 * Archivo: GlobalExceptionHandler.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.exception
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/04/2026
 * Modificaciones:
 * 09/04/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import co.edu.unipamplona.ciadti.rvd.model.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
   
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponseDTO( ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalStateException(
            IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex) {

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(
                        new ErrorResponseDTO(
                                "El tamaño de los archivos supera el límite permitido. "
                                        + "Cada archivo puede pesar máximo 10 MB y "
                                        + "el total de adjuntos por solicitud no puede superar 100 MB."
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO("Error interno del servidor: " + ex.getMessage()));
    }

}

/* 09/04/2026 @:Sebastian Jaimes */