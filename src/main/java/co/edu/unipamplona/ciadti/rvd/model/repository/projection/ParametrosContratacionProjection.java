/**
 * Aplicación: rvd
 * Archivo: ParametrosContratacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface ParametrosContratacionProjection {

    String getUnidadRegional();

    String getCoordinacion();

    String getModalidadContratacion();

    String getCategorizacion();

    String getPuntosAsignados();

    String getEstadoCategorizacion();

    BigDecimal getAsignacionSalarial();

    BigDecimal getValorHora();

    String getHorasTrabajo();
}
