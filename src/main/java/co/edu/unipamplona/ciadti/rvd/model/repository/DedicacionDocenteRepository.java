/**
 * Aplicación: rvd
 * Archivo: DedicacionDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.DedicacionDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface DedicacionDocenteRepository extends JpaRepository<DedicacionDocenteEntity, Long> {
    
    @Query(value = """
            SELECT
                DEDO.DEDO_ID AS id,
                DEDO.DEDO_DESCRIPCION AS label
            FROM ACADEMICO.DEDICACIONDOCENTE DEDO
            WHERE DEDO.DEDO_DESCRIPCION IS NOT NULL
            ORDER BY DEDO.DEDO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();
}
