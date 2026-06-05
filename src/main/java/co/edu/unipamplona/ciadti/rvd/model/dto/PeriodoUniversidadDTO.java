/**
 * Aplicación: rvd
 * Archivo: PeriodoUniversidadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/06/2026
 * Modificaciones:
 * 05/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record PeriodoUniversidadDTO(
    Long id,
    @JsonAlias("ano")
    Long anio,
    String periodo
) {}
/* 05/06/2026 @:Sebastian Jaimes */
