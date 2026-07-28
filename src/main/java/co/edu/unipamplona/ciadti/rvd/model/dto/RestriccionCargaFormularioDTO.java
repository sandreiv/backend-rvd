/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial 
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record RestriccionCargaFormularioDTO(
        Long idModalidadContratacion,
        String minimo,
        String maximo,
        String investigacion,
        String formaPago,
        String tipoHoras,
        List<Long> idsProgramasExcepcion,
        List<Long> idsPersonasExcepcion,
        Long idCategoriaCatedratico,
        Long idTipoActividad
) {}