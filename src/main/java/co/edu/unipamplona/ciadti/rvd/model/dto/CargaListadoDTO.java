package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record CargaListadoDTO(
    Long id,
    BigDecimal valor,
    BigDecimal valorAutorizado,
    EstadoCargaDTO estadoCarga
) {}
