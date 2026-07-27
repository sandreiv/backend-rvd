/**
 * Aplicación: rvd
 * Archivo: ProyectosRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;

public interface ProyectosRepository extends JpaRepository<ProyectosEntity, Long> {

    @Query(value = """
            SELECT
                PROY.PROY_ID AS id,
                PROY.COPR_ID AS idConvocatoriaProyectos,
                PROY.TIPR_ID AS idTipoProyecto,
                PROY.COOR_ID AS idCoordinacion,
                PROY.PROY_IDPROYECTO AS idProyectoPadre,
                PROY.PROY_NOMBRE AS nombre,
                PROY.PROY_DESCRIPCION AS descripcion,
                PROY.PROY_MONTO AS monto,
                PROY.PROY_FECHAINICIO AS fechaInicio,
                PROY.PROY_FECHAFIN AS fechaFin,
                COPR.COPR_NOMBRE AS nombreConvocatoriaProyectos,
                TIPR.TIPR_NOMBRE AS nombreTipoProyecto,
                COOR.COOR_NOMBRE AS nombreCoordinacion
            FROM RVD.PROYECTOS PROY
            LEFT JOIN RVD.CONVOCATORIAPROYECTOS COPR
                ON PROY.COPR_ID = COPR.COPR_ID
            LEFT JOIN RVD.TIPOPROYECTO TIPR
                ON PROY.TIPR_ID = TIPR.TIPR_ID
            LEFT JOIN RVD.COORDINACIONES COOR
                ON PROY.COOR_ID = COOR.COOR_ID
            WHERE PROY.PROY_IDPROYECTO IS NULL
            ORDER BY PROY.PROY_NOMBRE
            """, nativeQuery = true)
    List<ProyectosListaProjection> findParentProjectsList();

    @Query(value = """
            SELECT
                PROY.PROY_ID AS id,
                PROY.COPR_ID AS idConvocatoriaProyectos,
                PROY.TIPR_ID AS idTipoProyecto,
                PROY.COOR_ID AS idCoordinacion,
                PROY.PROY_IDPROYECTO AS idProyectoPadre,
                PROY.PROY_NOMBRE AS nombre,
                PROY.PROY_DESCRIPCION AS descripcion,
                PROY.PROY_MONTO AS monto,
                PROY.PROY_FECHAINICIO AS fechaInicio,
                PROY.PROY_FECHAFIN AS fechaFin,
                COPR.COPR_NOMBRE AS nombreConvocatoriaProyectos,
                TIPR.TIPR_NOMBRE AS nombreTipoProyecto,
                COOR.COOR_NOMBRE AS nombreCoordinacion
            FROM RVD.PROYECTOS PROY
            LEFT JOIN RVD.CONVOCATORIAPROYECTOS COPR
                ON PROY.COPR_ID = COPR.COPR_ID
            LEFT JOIN RVD.TIPOPROYECTO TIPR
                ON PROY.TIPR_ID = TIPR.TIPR_ID
            LEFT JOIN RVD.COORDINACIONES COOR
                ON PROY.COOR_ID = COOR.COOR_ID
            WHERE PROY.PROY_IDPROYECTO = :idProyecto
            ORDER BY PROY.PROY_NOMBRE
            """, nativeQuery = true)
    List<ProyectosListaProjection> findProductsByParentProjectId(
            @Param("idProyecto") Long idProyecto);

    boolean existsByIdTipoProyecto(Long idTipoProyecto);

    boolean existsByIdConvocatoriaProyectos(Long idConvocatoriaProyectos);

    boolean existsByIdProyectoPadre(Long idProyectoPadre);

    @Procedure(name = "ProyectosEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_PROY_ID") Long id,
            @Param("P_PROY_REGISTRADOPOR") String registradoPor
    );
}
