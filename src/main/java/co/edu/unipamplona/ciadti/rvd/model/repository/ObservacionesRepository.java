/**
 * Aplicación: rvd
 * Archivo: ObservacionesRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Observaciones del resumen de novedad de carga
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ObservacionResumenProjection;

public interface ObservacionesRepository
        extends JpaRepository<ObservacionesEntity, Long> {

    @Query(value = """
            SELECT
                OBSE.OBSE_ID AS idObservacion,
                OBSE.PEGE_IDREGISTRA AS idPersonaGeneral,
                TRIM(
                    PENG.PENG_PRIMERAPELLIDO || ' ' ||
                    NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                    PENG.PENG_PRIMERNOMBRE || ' ' ||
                    NVL(PENG.PENG_SEGUNDONOMBRE, '')
                ) AS nombrePersonaGeneral,
                OBSE.OBSE_TEXTO AS observacion,
                OBSE.OBSE_FECHA AS fecha
            FROM RVD.OBSERVACIONES OBSE
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = OBSE.PEGE_IDREGISTRA
            WHERE OBSE.CADO_ID = :idCargaDocente
            AND OBSE.OBSE_TEXTO IS NOT NULL
            AND TRIM(OBSE.OBSE_TEXTO) IS NOT NULL
            ORDER BY OBSE.OBSE_FECHA DESC, OBSE.OBSE_ID DESC
            """, nativeQuery = true)
    List<ObservacionResumenProjection> findByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);
}
