/**
 * Aplicación: rvd
 * Archivo: InformacionDocenteContratacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Andrés Hernández - Creación inicial
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

    String getFormaPago();

    String getEstadoCategorizacion();
    
    BigDecimal getAsignacionSalarial();
    
    String getHoras();

    String getCategoriaDocente();

    String getPuntos();

    String getExpedidoEn();

    String getTelefono();

    String getCelular();

    String getGenero();

    Date getFechaNacimiento();

    String getCiudadNacimiento();

    String getDepartamentoNacimiento();

    Long getIdCoordinacion();

    String getCoordinacion();

    String getUnidadRegional();

    String getFacultad();

    Long getIdPeriodoUniversidad();

    String getPrograma();

    Long getEsCoordinador();

    String getTituloMasculino();

    String getTituloFemenino();

    String getEstablecimiento();

    String getOtraInstitucion();

    Date getFechaObtencionTitulo();

    String getPaisTitulo();
}
