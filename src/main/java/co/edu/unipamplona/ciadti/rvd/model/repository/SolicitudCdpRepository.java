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
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HiringCdpInformationProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.SearchCdpResultsTesoreriaV3Projection;

public interface SolicitudCdpRepository
        extends JpaRepository<SolicitudCdpEntity, Long> {

    boolean existsByIdCoordinacionAndIdPeriodoUniversitario(
            Long idCoordinacion,
            Long idPeriodoUniversitario
    );

    boolean existsByNumero(String numero);

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
            ORDER BY RUBR.RUBR_ID ASC
            """, nativeQuery = true)
    List<SearchCdpResultsTesoreriaV3Projection> searchCdpsTesoreriaV3(
        String numeroCdp,
        Long idUnidad,
        Long anioVigencia
    );

    @Query(value = """
            SELECT
                TES.idCdp AS idCdpTesoreria,
                TES.numero AS numeroCdpTesoreria,
                TES.fecha AS fechaCdpTesoreria,
                TES.vigencia AS vigenciaCdpTesoreria,
                TES.rubros AS rubrosCdpTesoreria,
                SOCD.SOCD_ID AS idSolicitudCdpRvd
            FROM RVD.COORDINACIONES COOR_HIJA
            INNER JOIN RVD.COORDINACIONES COOR_PADRE
                ON COOR_PADRE.COOR_ID = COOR_HIJA.COOR_IDPADRE
            INNER JOIN RVD.SOLICITUDCDP SOCD
                ON SOCD.COOR_ID = COOR_PADRE.COOR_ID
            INNER JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = SOCD.PEUN_ID
            
            LEFT JOIN (
                SELECT
                    CDP.UNID_ID,
                    CDP.CDP_ID AS idCdp,
                    CDP.CDP_NUMERO AS numero,
                    CDP.CDP_FECHAEXPEDICION AS fecha,
                    CDP.CDP_VIGENCIA AS vigencia,
                    LISTAGG(
                        DISTINCT RUVI.RUVI_CODIGORUBRO,
                        ', '
                    ) WITHIN GROUP (
                        ORDER BY RUVI.RUVI_CODIGORUBRO
                    ) AS rubros
                FROM TESORERIAV3.CDP CDP

                INNER JOIN TESORERIAV3.CDPRUBRO CDRU
                    ON CDRU.CDP_ID = CDP.CDP_ID
                INNER JOIN TESORERIAV3.PRESUPUESTOEGRESOINVERSION PREI
                    ON PREI.PREI_ID = CDRU.PREI_ID
                INNER JOIN TESORERIAV3.RUBROVIGENCIA RUVI
                    ON RUVI.RUVI_ID = PREI.RUVI_ID
                INNER JOIN TESORERIAV3.RUBRO RUBR
                    ON RUBR.RUBR_ID = RUVI.RUBR_ID

                GROUP BY
                    CDP.UNID_ID,
                    CDP.CDP_ID,
                    CDP.CDP_NUMERO,
                    CDP.CDP_FECHAEXPEDICION,
                    CDP.CDP_VIGENCIA
            ) TES
                ON TES.numero = SOCD.SOCD_NUMERO
                AND TES.vigencia = PEUN.PEUN_ANO
                AND TES.UNID_ID = COOR_PADRE.UNID_IDREGIONAL

            WHERE COOR_HIJA.COOR_ID = :idCoordinacionHija
            AND SOCD.PEUN_ID = :idPeriodoUniversidad
            """, nativeQuery = true)
    HiringCdpInformationProjection getHiringCdpInformation(Long idCoordinacionHija, Long idPeriodoUniversidad);
}