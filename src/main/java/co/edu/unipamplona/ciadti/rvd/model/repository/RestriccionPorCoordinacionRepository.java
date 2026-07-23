package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionPorCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionRestriccionProjection;

public interface RestriccionPorCoordinacionRepository
        extends JpaRepository<RestriccionPorCoordinacionEntity, Long> {

    boolean existsByIdCoordinacionAndIdFechasConvocatoria(
            Long idCoordinacion,
            Long idFechasConvocatoria);

    boolean existsByIdCoordinacionAndIdFechasConvocatoriaAndIdNot(
            Long idCoordinacion,
            Long idFechasConvocatoria,
            Long id);

    @Query(value = """
            SELECT
                rexc.REXC_ID AS id,
                coor.COOR_ID AS idCoordinacion,
                coor.COOR_NOMBRE AS nombreCoordinacion,
                coor.COOR_DESCRIPCION AS descripcionCoordinacion,
                coor.COOR_CODIGO AS codigoCoordinacion,
                rexc.REXC_FECHAINICIO AS fechaInicio,
                rexc.REXC_FECHAFIN AS fechaFin,
                rexc.REXC_ESTADO AS estado
            FROM RVD.RESTRICCIONXCOORDINACION rexc
            INNER JOIN RVD.COORDINACIONES coor
                ON coor.COOR_ID = rexc.COOR_ID
            INNER JOIN RVD.FECHASCONVOCATORIA feco
                ON feco.FECO_ID = rexc.FECO_ID
            INNER JOIN RVD.CONVOCATORIA conv
                ON conv.CONV_ID = feco.CONV_ID
            WHERE conv.CONV_ID = :idConvocatoria
            ORDER BY coor.COOR_NOMBRE, rexc.REXC_FECHAINICIO
            """, nativeQuery = true)
    List<CoordinacionRestriccionProjection> findAllWithCoordinacion(
            @Param("idConvocatoria") Long idConvocatoria);

    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.RESTRICCIONXCOORDINACION REXC
            INNER JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = REXC.FECO_ID
            WHERE FECO.CONV_ID = :idConvocatoria
            AND REXC.REXC_ESTADO = '1'
            AND TRUNC(REXC.REXC_FECHAFIN) >= TRUNC(SYSDATE)
            """, nativeQuery = true)
    Long countActiveNonExpiredRestrictionsByConvocatoria(
            @Param("idConvocatoria") Long idConvocatoria);

    @Query(value = """
            SELECT FECO.CONV_ID
            FROM RVD.FECHASCONVOCATORIA FECO
            WHERE FECO.FECO_ID = :idFechasConvocatoria
            """, nativeQuery = true)
    Optional<Long> findConvocatoriaIdByFechaId(
            @Param("idFechasConvocatoria") Long idFechasConvocatoria);

    @Query(value = """
            SELECT FECO.CONV_ID
            FROM RVD.RESTRICCIONXCOORDINACION REXC
            INNER JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = REXC.FECO_ID
            WHERE REXC.REXC_ID = :idRestriccion
            """, nativeQuery = true)
    Optional<Long> findConvocatoriaIdByRestrictionId(
            @Param("idRestriccion") Long idRestriccion);

    @Query(value = """
            SELECT DISTINCT FECO.CONV_ID
            FROM RVD.RESTRICCIONXCOORDINACION REXC
            INNER JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = REXC.FECO_ID
            WHERE REXC.REXC_ESTADO = '1'
            """, nativeQuery = true)
    List<Long> findConvocatoriaIdsWithRestrictions();

    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.RESTRICCIONXCOORDINACION REXC
            INNER JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = REXC.FECO_ID
            WHERE FECO.CONV_ID = :idConvocatoria
            AND REXC.COOR_ID = :idCoordinacion
            AND REXC.REXC_ESTADO = '1'
            AND TRUNC(SYSDATE) BETWEEN TRUNC(REXC.REXC_FECHAINICIO)
                                    AND TRUNC(REXC.REXC_FECHAFIN)
            """, nativeQuery = true)
    Long countEditableRestrictionsByConvocatoriaAndCoordinacion(
            @Param("idConvocatoria") Long idConvocatoria,
            @Param("idCoordinacion") Long idCoordinacion);
            
}
