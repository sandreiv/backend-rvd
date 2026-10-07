/**
 * Aplicación: rvd
 * Archivo: RegistroNovedadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Registro de un lado de la comparativa: la novedad nueva o la anterior/carga original.
 */
public record RegistroNovedadDTO(
    String nombre,
    String documento,
    String modalidad,
    String puntos,
    String horasSemana,
    BigDecimal valorContrato,
    BigDecimal prestaciones,
    BigDecimal totalContrato,
    Map<String, BigDecimal> horasPorTipo
) {}
