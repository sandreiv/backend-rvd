package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;
import java.util.List;

public record CargaDocenteFormularioDTO(
    Long idPersonaGeneral,
    Long idModalidadContratacion,
    Long idCategoriaCatedratico,
    List<FechasConvocatoriaFormularioDTO> fechas,
    Long idTipoNovedad,
    Long idNovedad,
    Date fechaNovedad,
    String observacionNovedad,
    Date fechaInicio,
    Date fechaFin,
    String valorContrato,
    String valorPrestaciones,
    String asignacionSalarial,
    String estado,
    String vigente,
    String horas,
    String valorHora,
    String puntos,
    String valorPunto,
    String semanas,
    String nivelFormacion,
    String momento
) {}
