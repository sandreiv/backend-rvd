package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProgramaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface ProgramaRepository extends JpaRepository<ProgramaEntity, Long> {

    @Query(value = """
            SELECT DISTINCT
                id,
                nombre
            FROM (
                SELECT
                    prog.PROG_ID AS id,
                    prog.PROG_NOMBRE AS nombre
                FROM RVD.ASOCIACIONCOORDINACION asco
                INNER JOIN ACADEMICO.PROGRAMA prog
                    ON prog.PROG_ID = asco.PROG_ID
                INNER JOIN ACADEMICO.UNIDADPROGRAMA unpr
                    ON prog.PROG_ID = unpr.PROG_ID
                INNER JOIN ACADEMICO.UNIDAD unid
                    ON unpr.UNID_ID = unid.UNID_ID
                INNER JOIN ACADEMICO.MODALIDAD moda
                    ON prog.MODA_ID = moda.MODA_ID
                INNER JOIN ACADEMICO.NIVELEDUCATIVO nied
                    ON moda.NIED_ID = nied.NIED_ID
                WHERE asco.COOR_ID = :idCoordinacion
                    AND asco.MATE_CODIGOMATERIA IS NULL
                    AND unid.UNID_ID = :idUnidadRegional
                    AND nied.NIED_ID = :idNivelEducativo
                UNION
                SELECT
                    prog.PROG_ID AS id,
                    prog.PROG_NOMBRE AS nombre
                FROM RVD.ASOCIACIONCOORDINACION asco
                INNER JOIN ACADEMICO.PROGRAMA prog
                    ON prog.PROG_ID = asco.PROG_ID
                INNER JOIN ACADEMICO.MODALIDAD moda
                    ON prog.MODA_ID = moda.MODA_ID
                INNER JOIN ACADEMICO.NIVELEDUCATIVO nied
                    ON moda.NIED_ID = nied.NIED_ID
                WHERE asco.COOR_ID = :idCoordinacion
                    AND asco.MATE_CODIGOMATERIA IS NULL
                    AND nied.NIED_ID = :idNivelEducativo
                    AND NOT EXISTS (
                        SELECT 1
                        FROM ACADEMICO.UNIDADPROGRAMA unpr
                        WHERE unpr.PROG_ID = asco.PROG_ID
                            AND unpr.UNID_ID = :idUnidadRegional
                    )
            )
            ORDER BY nombre
            """, nativeQuery = true)
    List<ProgramaListadoProjection> findByCoordinacionUnidadRegionalAndNivelEducativo(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idUnidadRegional") Long idUnidadRegional,
            @Param("idNivelEducativo") Long idNivelEducativo);



    @Query(value = """
            SELECT
                PROG.PROG_ID AS id,
                PROG.PROG_NOMBRE AS label,
                PROG.PROG_CODIGOPROGRAMA AS codigo
            FROM ACADEMICO.PROGRAMA PROG
            WHERE PROG.PROG_NOMBRE IS NOT NULL
            ORDER BY PROG.PROG_NOMBRE
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();        


}
