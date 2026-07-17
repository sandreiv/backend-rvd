/**
 * Aplicación: rvd
 * Archivo: MetodologiaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.MetodologiaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface MetodologiaRepository extends JpaRepository<MetodologiaEntity, Long> {

    @Query(value = """
            SELECT
                METO.METO_ID AS id,
                METO.METO_DESCRIPCION AS label,
                NULL AS codigo
            FROM ACADEMICO.METODOLOGIA METO
            WHERE METO.METO_DESCRIPCION IS NOT NULL
            ORDER BY METO.METO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();
}

/* 17/07/2026 @:Daniel Arias */
