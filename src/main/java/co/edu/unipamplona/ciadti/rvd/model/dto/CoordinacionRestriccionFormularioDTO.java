/**
 * Aplicación: rvd
 * Archivo: CoordinacionRestriccionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/07/2026
 * Modificaciones:
 * 16/07/2026 - Sebastian Jaimes - Creación inicial
 * 27/08/2926 - Andrés Hernández - Manejo de fechas local
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record CoordinacionRestriccionFormularioDTO(
    Long idCoordinacion,
    Long idFechasConvocatoria,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String estado
) {}
