/**
 * Aplicación: rvd
 * Archivo: EstadoTrabajadorRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoTrabajadorEntity;

public interface EstadoTrabajadorRepository extends JpaRepository<EstadoTrabajadorEntity, Long> {
    
    @Query(value = """
            SELECT *
            FROM TALENTOV3.ESTADOTRABAJADOR ESTR
            WHERE ESTR.ESTR_INICIAL = '1'
            ORDER BY ESTR.ESTR_DESCRIPCION ASC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<EstadoTrabajadorEntity> findEstadoInicialTrabajador();
}
