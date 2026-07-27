/**
 * Aplicación: rvd
 * Archivo: ProyectosFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ProyectosFormularioDTO(
    String nombre,
    String descripcion,
    String monto,
    Date fechaInicio,
    Date fechaFin,
    Long idConvocatoriaProyectos,
    Long idTipoProyecto,
    Long idCoordinacion,
    Long idProyectoPadre
) {}
