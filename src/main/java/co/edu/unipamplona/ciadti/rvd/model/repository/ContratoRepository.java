/**
 * Aplicación: rvd
 * Archivo: ContratoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 * 18/11/2026 - Juan Diego Florez Vera - Búsqueda del contrato generado por docente y número de anexo
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;

public interface ContratoRepository extends JpaRepository<ContratoEntity, Long> {

    /**
     * Primer contrato realizado por un contratista dentro del contexto de 'RVD' (como docente)
     */
    @Query(value = """
            SELECT *
            FROM CONTRATOS.CONTRATO CONT
            WHERE CONT.PEGE_IDCONTRATISTA = :idPersona
                AND CONT.CONT_ORIGEN = 'RVD'
            ORDER BY CONT.CONT_FECHAINICIO ASC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<ContratoEntity> findFechaIngresoComoDocente(Long idPersona);

    /**
     * Contrato generado por RVD para un docente con el número de anexo indicado.
     * Sirve para actualizar el contrato en el paso de generación y para
     * recuperarlo en el paso de vinculación docente.
     */
    @Query(value = """
            SELECT *
            FROM CONTRATOS.CONTRATO CONT
            WHERE CONT.PEGE_IDCONTRATISTA = :idPersona
                AND CONT.CONT_NUMERO = :numero
                AND CONT.CONT_ORIGEN = 'RVD'
            ORDER BY CONT.CONT_ID DESC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<ContratoEntity> findByDocenteYNumero(
            @Param("idPersona") Long idPersona,
            @Param("numero") String numero);
}
