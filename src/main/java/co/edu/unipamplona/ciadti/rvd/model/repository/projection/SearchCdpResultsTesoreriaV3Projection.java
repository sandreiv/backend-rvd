/**
 * Aplicación: rvd
 * Archivo: SearchCdpResultsTesoreriaV3.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/10/2026
 * Modificaciones:
 * 20/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDate;

public interface SearchCdpResultsTesoreriaV3Projection {
    Long getIdRubro();
    Long getIdCdp();
    String getNumero();
    LocalDate getFecha();
    String getVigencia();
    String getDescripcion();
    String getArticulo();
}
