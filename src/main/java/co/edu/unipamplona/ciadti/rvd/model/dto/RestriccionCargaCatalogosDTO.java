/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaCatalogosDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para catálogos del formulario de restricción de carga.
 * 05/11/2026 - Andrés Hernández - Adición de las diferentes dedicaciones y clasificaciones del docente
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record RestriccionCargaCatalogosDTO(
        List<CatalogoAdministracionDTO> dedicaciones,
        List<CatalogoAdministracionDTO> clasificaciones,
        List<CatalogoAdministracionDTO> categorias,
        List<CatalogoAdministracionDTO> tiposActividad,
        List<CatalogoAdministracionDTO> programas,
        List<CatalogoAdministracionDTO> personas
) {}