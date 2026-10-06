/**
 * Aplicación: rvd
 * Archivo: ReporteNovedadCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

/**
 * Comparativa para el PDF: novedad nueva (actual) vs registro anterior
 * (novedad aprobada previa o carga docente original).
 */
public record ReporteNovedadCargaDTO(
    EncabezadoCargaReporteDTO encabezado,
    String tipoNovedad,
    RegistroNovedadDTO anterior,
    RegistroNovedadDTO actual,
    String generadoPor,
    String fechaGeneracion
) {}
