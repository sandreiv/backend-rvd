/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaDetalleDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial
 * 06/08/2026 - Se agrega programasExcepcion con máximo de horas por programa.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record RestriccionCargaDetalleDTO(
        Long idModalidadContratacion,
        String minimo,
        String maximo,
        String investigacion,
        String formaPago,
        String tipoContrato,
        String tipoHoras,
        List<Long> idsProgramasExcepcion,
        List<RestriccionCargaProgramaExcepcionDTO> programasExcepcion,
        List<Long> idsPersonasExcepcion,
        List<RestriccionCargaPersonaExcepcionDTO> personasExcepcion,
        List<Long> idsCategoriasCatedratico,
        List<Long> idsTiposActividad
) {}
