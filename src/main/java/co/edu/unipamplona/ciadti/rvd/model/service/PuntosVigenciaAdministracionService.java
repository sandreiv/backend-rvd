/**
 * Aplicación: rvd
 * Archivo: PuntosVigenciaAdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaListadoDTO;

public interface PuntosVigenciaAdministracionService {

    List<PuntosVigenciaListadoDTO>
            listPointsValidity();

    void savePointsValidity(
            PuntosVigenciaFormularioDTO dto
    );

    void updatePointsValidity(
            Long id,
            PuntosVigenciaFormularioDTO dto
    );

    void deletePointsValidity(
            Long id
    );

    void deleteBulkPointsValidity(
            List<Long> ids
    );
}