/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaAdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para administrar restricciones de carga por modalidad.
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaModalidadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaFormularioDTO;

public interface RestriccionCargaAdministracionService {

    List<RestriccionCargaModalidadListadoDTO> listModalities();

    RestriccionCargaCatalogosDTO getCatalogs();

    RestriccionCargaDetalleDTO getRestriction(Long idModalidadContratacion);

    void saveRestriction(RestriccionCargaFormularioDTO dto);
    
}