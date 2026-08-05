/**
 * Aplicación: rvd
 * Archivo: DocentePreasignacionReporteProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 04/08/2026 - Sebastian Jaimes - Campos de contrato y fechas para reporte batch
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface DocentePreasignacionReporteProjection {

    Long getIdCargaDocente();

    Long getIdPersonaGeneral();

    String getDocumento();

    String getNombreCompleto();

    String getEstado();

    Long getIdModalidadContratacion();

    String getModalidadContratacion();

    Long getIdCategoriaCatedratico();

    String getCategoria();

    String getPuntos();

    BigDecimal getValorPunto();

    BigDecimal getSalario();

    Date getFechaInicio();

    Date getFechaFin();

    String getSemanas();
}
