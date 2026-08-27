/**
 * Aplicación: rvd
 * Archivo: HorasActividadPadreProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface HorasActividadPadreProjection {

    String getCodigo();

    String getNombre();

    BigDecimal getHoras();
}
