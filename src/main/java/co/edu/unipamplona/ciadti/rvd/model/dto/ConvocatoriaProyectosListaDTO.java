/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 29/10/2026 - Andrés Hernández - Rango de fechas para la convocatoria
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record ConvocatoriaProyectosListaDTO(
    Long id,
    String nombre,
    String descripcion,
    String codigo,
    Long idConvocatoria,
    String nombreConvocatoria,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {}
