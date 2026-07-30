package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;
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
                tiac.TIAC_CODIGO AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad
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

    @Query(value = """
            SELECT
                PEPR.PEPR_ID AS id,
                PEPR.PROY_ID AS idProyecto,
                PEPR.PEGE_ID AS idPersonaGeneral,
                COALESCE(
                    NULLIF(TRIM(
                        NVL(PENG.PENG_PRIMERAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_PRIMERNOMBRE, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '')
                    ), ''),
                    'Persona ' || PEPR.PEGE_ID
                ) AS nombreCompleto,
                PEPR.TIAC_ID AS idTipoActividad,
                TIAC.TIAC_NOMBRE AS nombreTipoActividad,
                PEPR.PEPR_TIPO AS tipo,
                PEPR.PEPR_HORAS AS horas,
                PEPR.PEPR_OBSERVACION AS observacion
            FROM RVD.PERSONAPROYECTO PEPR
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEPR.PEGE_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = PEPR.TIAC_ID
            WHERE PEPR.PROY_ID = :idProyecto
            ORDER BY nombreCompleto, PEPR.PEPR_ID
            """, nativeQuery = true)
    List<PersonaProyectoListaProjection> findByIdProyecto(
            @Param("idProyecto") Long idProyecto);

    boolean existsByIdAndIdProyecto(Long id, Long idProyecto);

    boolean existsByIdProyecto(Long idProyecto);

    @Procedure(name = "PersonaProyectoEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_PEPR_ID") Long id,
            @Param("P_PEPR_REGISTRADOPOR") String registradoPor
    );
}
