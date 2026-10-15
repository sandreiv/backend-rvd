/**
 * Aplicación: rvd
 * Archivo: InformacionDocenteContratacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Andrés Hernández - Creación inicial
 * 15/10/2026 - Sebastian Jaimes - Estado, salario, valor hora y horas de trabajo
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface InformacionDocenteContratacionProjection {

    Long getIdPersonaGeneral();

    String getNombreCompleto();

    String getDocumentoIdentidad();

    String getDireccionDomicilio();

    String getCorreoPersonal();

    String getCorreoInstitucional();

    String getModalidadContratacion();

    Date getFechaInicio();

    Date getFechaFin();

    String getCategoriaDocente();

    String getPuntos();

    String getEstadoCategorizacion();

    BigDecimal getAsignacionSalarial();

    BigDecimal getValorHora();

    String getHorasTrabajo();
}
