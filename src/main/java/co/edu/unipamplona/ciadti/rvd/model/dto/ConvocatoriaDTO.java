/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/06/2026
 * Modificaciones:
 * 03/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ConvocatoriaDTO(
    String descripcion,
    Date fechaInicio,
    Date fechaFin,
    String nombreCompleto
) {}
/* 03/06/2026 @:Sebastian Jaimes */