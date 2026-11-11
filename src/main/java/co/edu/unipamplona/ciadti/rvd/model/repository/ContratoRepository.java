/**
 * Aplicación: rvd
 * Archivo: ContratoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;

public interface ContratoRepository extends JpaRepository<ContratoEntity, Long> {

    /**
     * Primer contrato realizado por un contratista dentro del contexto de 'RVD' (como docente)
     */
    @Query(value = """
            SELECT *
            FROM CONTRATOS.CONTRATO
            WHERE PEGE_IDCONTRATISTA = :idPersona
                AND CONT_ORIGEN = 'RVD'
            ORDER BY CONT_FECHAINICIO ASC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<ContratoEntity> findFechaIngresoComoDocente(Long idPersona);
}
