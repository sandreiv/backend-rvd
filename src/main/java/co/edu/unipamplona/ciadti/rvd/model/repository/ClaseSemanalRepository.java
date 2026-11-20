/**
 * Aplicación: rvd
 * Archivo: ClaseSemanalRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ClaseSemanalEntity;

public interface ClaseSemanalRepository extends JpaRepository<ClaseSemanalEntity, Long> {
    
    @Query(value = """
            SELECT CLSE.*
            FROM ACADEMICO.CLASESEMANAL CLSE
            WHERE CLSE.GRUP_ID IN (:idsGrupos)
            """, nativeQuery = true)
    List<ClaseSemanalEntity> findAllClasesByGrupos(@Param("idsGrupos") List<Long> idsGrupos);
}
