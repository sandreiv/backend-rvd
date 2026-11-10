/**
 * Aplicación: rvd
 * Archivo: ClasificacionDocenteRepository.java
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

import co.edu.unipamplona.ciadti.rvd.model.entity.ClasificacionDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface ClasificacionDocenteRepository extends JpaRepository<ClasificacionDocenteEntity, Long> {
    
    @Query(value = """
            SELECT
                CLDO.CLDO_ID AS id,
                CLDO.CLDO_DESCRIPCION AS label
            FROM ACADEMICO.CLASIFICACIONDOCENTE CLDO
            WHERE CLDO.CLDO_DESCRIPCION IS NOT NULL
            ORDER BY CLDO.CLDO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();
}
