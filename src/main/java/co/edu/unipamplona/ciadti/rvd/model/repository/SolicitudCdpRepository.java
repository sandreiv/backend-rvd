/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.SolicitudCdpEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.SearchCdpResultsTesoreriaV3Projection;

public interface SolicitudCdpRepository
        extends JpaRepository<SolicitudCdpEntity, Long> {

    boolean existsByIdCoordinacionAndIdPeriodoUniversitario(
            Long idCoordinacion,
            Long idPeriodoUniversitario
    );

    Optional<SolicitudCdpEntity>
            findByIdCoordinacionAndIdPeriodoUniversitario(
                    Long idCoordinacion,
                    Long idPeriodoUniversitario
            );

    @Query(value = """
            SELECT
                CDP.CDP_NUMERO as numero,
                CDP.CDP_FECHAEXPEDICION as fecha,
                CDP.CDP_VIGENCIA as vigencia,
                CDP.CDP_CONCEPTO as descripcion,
                RUVI.RUVI_CODIGORUBRO as articulo,
                RUBR.RUBR_ID as idRubro,
                CDP.CDP_ID as idCdp
            FROM TESORERIAV3.CDP CDP
            INNER JOIN TESORERIAV3.CDPRUBRO CDRU
                ON CDRU.CDP_ID = CDP.CDP_ID
            INNER JOIN TESORERIAV3.PRESUPUESTOEGRESOINVERSION PREI
                ON PREI.PREI_ID = CDRU.PREI_ID
            INNER JOIN TESORERIAV3.RUBROVIGENCIA RUVI
                ON RUVI.RUVI_ID = PREI.RUVI_ID
            INNER JOIN TESORERIAV3.RUBRO RUBR
                ON RUBR.RUBR_ID = RUVI.RUBR_ID
            WHERE CDP.CDP_NUMERO = :numeroCdp
            AND CDP.CDP_VIGENCIA = :anioVigencia
            AND CDP.UNID_ID = :idUnidad
            """, nativeQuery = true)
    List<SearchCdpResultsTesoreriaV3Projection> searchCdpsTesoreriaV3(
        String numeroCdp,
        Long idUnidad,
        Long anioVigencia
    );
}