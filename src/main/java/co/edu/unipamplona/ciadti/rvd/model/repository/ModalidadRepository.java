/**
 * Aplicación: rvd
 * Archivo: ModalidadRepository.java
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

import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface ModalidadRepository extends JpaRepository<ModalidadEntity, Long> {

    @Query(value = """
            SELECT
                MODA.MODA_ID AS id,
                MODA.MODA_DESCRIPCION AS label,
                MODA.MODA_CODIGO AS codigo
            FROM ACADEMICO.MODALIDAD MODA
            WHERE MODA.MODA_DESCRIPCION IS NOT NULL
            ORDER BY MODA.MODA_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();
}

/* 17/07/2026 @:Daniel Arias */
