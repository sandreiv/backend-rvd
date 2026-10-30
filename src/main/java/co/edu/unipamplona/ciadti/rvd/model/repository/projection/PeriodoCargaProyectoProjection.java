/**
 * Aplicación: rvd
 * Archivo: PeriodoCargaProyectoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/10/2026
 * Modificaciones:
 * 29/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDate;

public interface PeriodoCargaProyectoProjection {
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
}
