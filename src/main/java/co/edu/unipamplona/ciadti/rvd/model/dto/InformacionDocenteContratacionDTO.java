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
    String formaPago,
    String estadoCategorizacion,
    BigDecimal asignacionSalarial,
    String horas,
    String categoriaDocente,
    String puntos,
    String expedidoEn,
    String telefono,
    String celular,
    String genero,
    Date fechaNacimiento,
    String ciudadNacimiento,
    String departamentoNacimiento,
    Long idCoordinacion,
    String coordinacion,
    String unidadRegional,
    String facultad,
    Long idPeriodoUniversidad,
    String programa,
    Long esCoordinador,
    String tituloMasculino,
    String tituloFemenino,
    String establecimiento,
    String otraInstitucion,
    Date fechaObtencionTitulo,
    String paisTitulo,
    List<ActividadHorasResumenDTO> horasActividades
) {}
