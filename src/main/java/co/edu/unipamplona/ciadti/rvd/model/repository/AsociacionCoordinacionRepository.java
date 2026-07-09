package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.AsociacionCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.AsociacionCoordinacionListadoProjection;

public interface AsociacionCoordinacionRepository
        extends JpaRepository<AsociacionCoordinacionEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);

    @Query(value = """
            SELECT
                ASCO.ASCO_ID AS id,
                COOR.COOR_ID AS idCoordinacion,
                COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION) AS coordinacion,
                PROG.PROG_ID AS idPrograma,
                PROG.PROG_NOMBRE AS programa,
                ASCO.MATE_CODIGOMATERIA AS codigoMateria,
                MATE.MATE_NOMBRE AS materia,
                CECO.CECO_ID AS idCentroCosto,
                CASE
                    WHEN CECO.CECO_ID IS NULL THEN NULL
                    ELSE CECO.CECO_DESCRIPCION
                END AS centroCosto,
                ASCO.ASCO_ESTADO AS estado
            FROM RVD.ASOCIACIONCOORDINACION ASCO
            LEFT JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = ASCO.COOR_ID
            LEFT JOIN ACADEMICO.PROGRAMA PROG
                ON PROG.PROG_ID = ASCO.PROG_ID
            LEFT JOIN ACADEMICO.MATERIA MATE
                ON MATE.MATE_CODIGOMATERIA = ASCO.MATE_CODIGOMATERIA
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = ASCO.CECO_ID
            ORDER BY COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION), PROG.PROG_NOMBRE, MATE.MATE_NOMBRE
            """, nativeQuery = true)
    List<AsociacionCoordinacionListadoProjection> findAdministrationList();

    @Query(value = """
            SELECT
                COUNT(*)
            FROM RVD.ASOCIACIONCOORDINACION asco
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.PROG_ID = :idPrograma
                AND asco.MATE_CODIGOMATERIA IS NULL
                AND NOT EXISTS (
                    SELECT 1
                    FROM ACADEMICO.UNIDADPROGRAMA unpr
                    WHERE unpr.PROG_ID = asco.PROG_ID
                        AND unpr.UNID_ID = :idUnidadRegional
                )
            """, nativeQuery = true)
    Long countProgramaTransversal(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idPrograma") Long idPrograma,
            @Param("idUnidadRegional") Long idUnidadRegional);

    default boolean isProgramaTransversal(
            Long idCoordinacion,
            Long idPrograma,
            Long idUnidadRegional) {
        Long count = countProgramaTransversal(
                idCoordinacion, idPrograma, idUnidadRegional);
        return count != null && count > 0;
    }
}