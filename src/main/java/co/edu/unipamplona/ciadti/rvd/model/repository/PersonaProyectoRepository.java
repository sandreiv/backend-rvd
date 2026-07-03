package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectoDocenteListadoProjection;

public interface PersonaProyectoRepository
        extends JpaRepository<PersonaProyectoEntity, Long> {

    @Query(value = """
            SELECT
                proy.PROY_ID AS id,
                proy.PROY_IDPROYECTO AS idProyecto,
                proy.PROY_NOMBRE AS nombre,
                proy.PROY_DESCRIPCION AS descripcion,
                tipr.TIPR_ID AS idTipoProyecto,
                tipr.TIPR_NOMBRE AS nombreTipoProyecto,
                tipr.TIPR_DESCRIPCION AS descripcionTipoProyecto,
                tipr.TIPR_TIPO AS tipoTipoProyecto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PEGE_ID AS idPersonaGeneral,
                pepr.PEPR_HORAS AS horas,
                pepr.PEPR_TIPO AS tipo,
                pepr.PEPR_OBSERVACION AS observacion,
                tiac.TIAC_ID AS idTipoActividad,
                tiac.TIAC_IDPADRE AS idPadreTipoActividad,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                tiac.TIAC_CODIGO AS codigoTipoActividad
            FROM RVD.PROYECTOS proy
            INNER JOIN RVD.PERSONAPROYECTO pepr
                ON proy.PROY_ID = pepr.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr
                ON proy.TIPR_ID = tipr.TIPR_ID
            LEFT JOIN RVD.TIPOACTIVIDADES tiac
                ON pepr.TIAC_ID = tiac.TIAC_ID
            WHERE pepr.PEGE_ID = :idPersonaGeneral
            ORDER BY proy.PROY_NOMBRE, pepr.PEPR_ID
            """, nativeQuery = true)
    List<ProyectoDocenteListadoProjection> findProyectosByIdPersonaGeneral(
            @Param("idPersonaGeneral") Long idPersonaGeneral);
}
