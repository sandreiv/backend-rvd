/**
 * Aplicación: rvd
 * Archivo: TrabajadorLaborUnidadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorLaborUnidadEntity;

public interface TrabajadorLaborUnidadRepository extends JpaRepository<TrabajadorLaborUnidadEntity, Long> {
    
    boolean existsByIdPersonaGeneralAndIdDocumentoVinculacionAndIdLaborAndIdUnidad(Long idPersonaGeneral, Long idDocumentoVinculacion, Long idLabor, Long idUnidad);
}
