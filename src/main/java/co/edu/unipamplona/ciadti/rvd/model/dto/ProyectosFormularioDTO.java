/**
 * Aplicación: rvd
 * Archivo: ProyectosFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 27/08/2026 - Andrés Hernández - Manejo de fechas local
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record ProyectosFormularioDTO(
    String nombre,
    String descripcion,
    String monto,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    Long idConvocatoriaProyectos,
    Long idTipoProyecto,
    Long idCoordinacion,
    Long idProyectoPadre
) {}
