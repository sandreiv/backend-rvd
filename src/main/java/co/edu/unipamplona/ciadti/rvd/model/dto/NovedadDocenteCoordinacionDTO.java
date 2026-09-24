package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record NovedadDocenteCoordinacionDTO(
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
