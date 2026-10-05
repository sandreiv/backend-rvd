/**
 * Aplicación: rvd
 * Archivo: DocenteCoordinacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 * 21/08/2026 - Andrés Hernández - Comprobación de actividades en la carga docente
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record DocenteCoordinacionDTO(
    Long idCargaDocente,
    Long idPersonaGeneral,
    String nombreCompleto,
    String estado,
    Long idModalidadContratacion,
    Long idCategoriaCatedratico,
    Long idCarga,
    Long idFechasConvocatoria,
    String fechaConvocatoriaCodigo,
    Date fechaInicio,
    Date fechaFin,
    BigDecimal valorContrato,
    BigDecimal valorPrestaciones,
    BigDecimal asignacionSalarial,
    BigDecimal totalContrato,
    BigDecimal valorHora,
    String puntos,
    BigDecimal valorPunto,
    String semanas,
    String onceMeses,
    String horasDeExcepcion,
    Boolean tieneCarga,
    Boolean tieneDetalleActividades
) {}
