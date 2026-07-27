/**
 * Aplicación: rvd
 * Archivo: TipoProyectoListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoProyectoListaDTO(
    Long id,
    String nombre,
    String descripcion,
    String minimoParticipantes,
    String maximoParticipantes,
    String montoMaximo,
    String minimoProductos,
    String minimoConocimientoTi,
    String tipo
) {}
