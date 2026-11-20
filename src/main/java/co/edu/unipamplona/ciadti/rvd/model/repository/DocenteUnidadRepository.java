/**
 * Aplicación: rvd
 * Archivo: DocenteUnidadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteUnidadEntity;

public interface DocenteUnidadRepository extends JpaRepository<DocenteUnidadEntity, Long> {
    
    Optional<DocenteUnidadEntity> findByIdPersonaGeneralAndIdLaborAndIdDocumentoVinculacionAndIdUnidad(
        Long idPersonaGeneral,
        Long idLabor,
        Long idDocumentoVinculacion,
        Long idUnidad
    );
}
