/**
 * Aplicación: rvd
 * Archivo: CargaDocenteFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/06/2026
 * Modificaciones:
 * 25/06/2026 - Sebastian Jaimes - Creación inicial
 * 27/08/2026 - Andrés Hernández - Manejo de fechas local
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CargaDocenteFormularioDTO(
    Long idPersonaGeneral,
    Long idModalidadContratacion,
    Long idCategoriaCatedratico,
    Long idCarga,
    FechasConvocatoriaFormularioDTO fechasConvocatoria,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    BigDecimal valorContrato,
    BigDecimal valorPrestaciones,
    BigDecimal asignacionSalarial,
    BigDecimal totalContrato,
    BigDecimal valorHora,
    String puntos,
    BigDecimal valorPunto,
    String semanas,
    String horasDeExcepcion
) {}
