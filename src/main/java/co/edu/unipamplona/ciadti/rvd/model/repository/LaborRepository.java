/**
 * Aplicación: rvd
 * Archivo: LaborRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.LaborEntity;

public interface LaborRepository extends JpaRepository<LaborEntity, Long> {
    
    @Query(value = """
            SELECT *
            FROM TALENTOV3.LABOR LABO
            WHERE LABO.LABO_DOCENTE = '1'
            ORDER BY LABO.LABO_NOMBRE ASC
            """, nativeQuery = true)
    List<LaborEntity> findAllLaboresDocente();
}
