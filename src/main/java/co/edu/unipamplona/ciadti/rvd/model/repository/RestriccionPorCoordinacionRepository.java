package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

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
}
