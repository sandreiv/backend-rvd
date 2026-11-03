/**
 * Aplicación: rvd
 * Archivo: ContratoPlanTrabajoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (generación del anexo de contratación en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto.contratacion;

import java.math.BigDecimal;
import java.util.List;

public record ContratoPlanTrabajoDTO(
    List<ContratoMateriaDirectaDTO> directo,
    BigDecimal totalHorasDirecto,
    List<ContratoActividadDTO> indirecto,
    BigDecimal totalHorasIndirecto,
    ContratoCteiDTO ctei,
    List<ContratoActividadDTO> isu,
    BigDecimal totalHorasIsu,
    List<ContratoActividadDTO> administracion,
    BigDecimal totalHorasAdministracion,
    String observaciones,
    BigDecimal totalHorasRelacionadas
) {}
