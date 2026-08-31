/**
 * Aplicación: rvd
 * Archivo: GrupoCuposReporteProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 31/08/2026
 * Modificaciones:
 * 31/08/2026 - Sebastian Jaimes - Creación inicial
 * 31/08/2026 - Sebastian Jaimes - Conteo de grupos en lugar de nombres
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface GrupoCuposReporteProjection {

    Long getIdCargaDocente();

    BigDecimal getCantidadGrupos();

    BigDecimal getCupos();
}
