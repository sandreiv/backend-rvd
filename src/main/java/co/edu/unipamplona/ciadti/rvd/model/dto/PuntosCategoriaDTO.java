/**
 * Aplicación: rvd
 * Archivo: PuntosCategoriaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/06/2026
 * Modificaciones:
 * 23/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PuntosCategoriaDTO(
    Long id,
    Long idCategoriaCatedratico,
    String puntos
) {
    
}
