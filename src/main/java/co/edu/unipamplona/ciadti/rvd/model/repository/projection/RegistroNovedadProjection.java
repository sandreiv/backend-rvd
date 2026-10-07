/**
 * Aplicación: rvd
 * Archivo: RegistroNovedadProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad)
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

/**
 * Foto de un registro (novedad o carga original) para la comparativa del PDF.
 */
public interface RegistroNovedadProjection {

    String getNombre();

    String getDocumento();

    String getModalidad();

    String getPuntos();

    String getHorasSemana();

    BigDecimal getValorContrato();

    BigDecimal getPrestaciones();

    BigDecimal getTotalContrato();

    String getTipoNovedad();
}
