package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record CargaDocenteFormularioDTO(
    Long idPersonaGeneral,
    Long idModalidadContratacion,
    Long idCategoriaCatedratico,
    Long idCarga,
    FechasConvocatoriaFormularioDTO fechasConvocatoria,
    Date fechaInicio,
    Date fechaFin,
    BigDecimal valorContrato,
    BigDecimal valorPrestaciones,
    BigDecimal asignacionSalarial,
    BigDecimal totalContrato,
    BigDecimal valorHora,
    String puntos,
    BigDecimal valorPunto,
    String semanas
) {}
