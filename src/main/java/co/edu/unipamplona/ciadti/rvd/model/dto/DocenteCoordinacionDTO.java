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
    Boolean tieneCarga
) {}
