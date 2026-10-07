/**
 * Aplicación: rvd
 * Archivo: DocenteEfectivoCoordinacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/09/2026
 * Modificaciones:
 * 16/09/2026 - Andrés Hernández - Creación inicial
 * 05/10/2026 - Andrés Hernández - Adición campo IdNovedadCargaDocente
 * 07/10/2026 - Andrés Hernández - Manejo global para docentes efectivos
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record DocenteEfectivoCoordinacionDTO(
    Long idNovedadCargaDocente,
    Long idCargaDocente,
    Long idPersonaGeneral,
    String nombreCompleto,
    String estado,
    Long idModalidadContratacion,
    Long idCategoriaCatedratico,
    Long idCarga,
    Long idNovedadCatalogo,
    String estadoNovedad,
    String tipoNovedad,
    String motivoRechazo,
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
