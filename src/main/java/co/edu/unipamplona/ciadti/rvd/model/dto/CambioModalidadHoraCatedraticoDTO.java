package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record CambioModalidadHoraCatedraticoDTO(
        Long idCargaDocente,
        Long idCarga,
        Long idPersonaGeneral,
        Long idModalidadContratacion,
        Long idCategoriaCatedratico,
        Long idNovedad,
        FechasConvocatoriaFormularioDTO fechasConvocatoria,
        String semanas,
        String horas,
        String horasDeExcepcion,
        BigDecimal valorHora,
        String puntos,
        BigDecimal valorPunto,
        BigDecimal valorContrato,
        BigDecimal valorPrestaciones,
        BigDecimal totalContrato,
        BigDecimal asignacionSalarial,
        String onceMeses,
        List<DetalleCargaDocenteItemDTO> detalles
) {}
