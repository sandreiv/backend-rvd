package co.edu.unipamplona.ciadti.rvd.model.dto;

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
    String valorContrato,
    String valorPrestaciones,
    String asignacionSalarial,
    String totalContrato,
    String valorHora,
    String puntos,
    String valorPunto,
    String semanas,
    Boolean tieneCarga
) {}
