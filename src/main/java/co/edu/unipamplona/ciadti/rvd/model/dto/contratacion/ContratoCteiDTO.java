/**
 * Aplicación: rvd
 * Archivo: ContratoCteiDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (generación del anexo de contratación en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto.contratacion;

import java.math.BigDecimal;
import java.util.List;

public record ContratoCteiDTO(
    String descripcionProyecto,
    List<String> productos,
    List<ContratoActividadDTO> dedicacion,
    BigDecimal totalHoras
) {}
