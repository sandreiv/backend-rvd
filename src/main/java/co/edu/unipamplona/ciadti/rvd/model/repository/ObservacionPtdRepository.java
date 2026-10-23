/**
 * Aplicación: rvd
 * Archivo: ObservacionPtdRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/10/2026
 * Modificaciones:
 * 14/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionPtdEntity;

public interface ObservacionPtdRepository extends JpaRepository<ObservacionPtdEntity, Long> {
    
    @Query(value = """
            SELECT OBPT.*
            FROM RVD.OBSERVACIONPTD OBPT
            WHERE OBPT.CADO_ID = :idCargaDocente
                AND OBPT_PEGEROL = :rolPersonaRegistra
            """, nativeQuery = true)
    ObservacionPtdEntity findByIdCargaDocenteAndPegeRole(@Param("idCargaDocente") Long idCargaDocente, @Param("rolPersonaRegistra") String rolPersonaRegistra);
}
