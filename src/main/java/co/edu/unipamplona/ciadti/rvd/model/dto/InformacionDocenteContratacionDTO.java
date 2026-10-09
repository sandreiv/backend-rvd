/**
 * Aplicación: rvd
 * Archivo: InformacionDocenteContratacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Sebastian Jaimes  - Creación inicial
 * 08/10/2026 - Sebastian Jaimes - Incluye horas de actividades PTD vigentes
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;
import java.util.List;

public record InformacionDocenteContratacionDTO(
    Long idPersonaGeneral,
    String nombreCompleto,
    String documentoIdentidad,
    String direccionDomicilio,
    String correoPersonal,
    String correoInstitucional,
    String modalidadContratacion,
    Date fechaInicio,
    Date fechaFin,
    String categoriaDocente,
    String puntos,
    List<ActividadHorasResumenDTO> horasActividades
) {}
