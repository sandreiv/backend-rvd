/**
 * Aplicación: rvd
 * Archivo: InformacionDocenteContratacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Sebastian Jaimes  - Creación inicial
 * 08/10/2026 - Sebastian Jaimes - Incluye horas de actividades PTD vigentes
 * 15/10/2026 - Sebastian Jaimes - Estado, salario, valor hora y horas de trabajo
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
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
    String estadoCategorizacion,
    BigDecimal asignacionSalarial,
    BigDecimal valorHora,
    String horasTrabajo,
    List<ActividadHorasResumenDTO> horasActividades
) {}
