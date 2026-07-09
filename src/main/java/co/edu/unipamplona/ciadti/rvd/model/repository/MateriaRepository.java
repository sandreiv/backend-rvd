package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.MateriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaCatalogoAdministracionProjection;

public interface MateriaRepository extends JpaRepository<MateriaEntity, String> {

    @Query(value = """
            SELECT
                mate.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombre,
                mate.MATE_HORASPRACTICAS AS horasPracticas,
                mate.MATE_HORASTEORICAS AS horasTeoricas,
                mate.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                pema.PEMA_PERIODO AS periodo,
                mate.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                grup.GRUP_CAPACIDAD AS capacidad
            FROM ACADEMICO.MATERIA mate
            LEFT JOIN ACADEMICO.PENSUMMATERIA pema
                ON mate.MATE_CODIGOMATERIA = pema.MATE_CODIGOMATERIA
            LEFT JOIN ACADEMICO.PROGRAMA prog
                ON pema.PROG_ID = prog.PROG_ID
            LEFT JOIN ACADEMICO.GRUPO grup
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
            WHERE prog.PROG_ID = :idPrograma
                AND mate.MATE_CODIGOMATERIA NOT IN (
                    SELECT asco.MATE_CODIGOMATERIA
                    FROM RVD.ASOCIACIONCOORDINACION asco
                    WHERE asco.MATE_CODIGOMATERIA IS NOT NULL
                )
            ORDER BY mate.MATE_NOMBRE, pema.PEMA_PERIODO, grup.GRUP_NOMBRE
            """, nativeQuery = true)
    List<MateriaListadoProjection> findNoTransversalesByPrograma(
            @Param("idPrograma") Long idPrograma);

    @Query(value = """
            SELECT
                mate.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombre,
                mate.MATE_HORASPRACTICAS AS horasPracticas,
                mate.MATE_HORASTEORICAS AS horasTeoricas,
                mate.MATE_HORASTEORICOPRACTICAS AS horasTeoricoPracticas,
                pema.PEMA_PERIODO AS periodo,
                mate.MATE_PONDERACIONACADEMICA AS ponderacionAcademica,
                grup.GRUP_CAPACIDAD AS capacidad
            FROM ACADEMICO.MATERIA mate
            INNER JOIN RVD.ASOCIACIONCOORDINACION asco
                ON mate.MATE_CODIGOMATERIA = asco.MATE_CODIGOMATERIA
            LEFT JOIN ACADEMICO.PENSUMMATERIA pema
                ON mate.MATE_CODIGOMATERIA = pema.MATE_CODIGOMATERIA
                AND pema.PROG_ID = asco.PROG_ID
            LEFT JOIN ACADEMICO.GRUPO grup
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
            WHERE asco.COOR_ID = :idCoordinacion
                AND asco.PROG_ID = :idPrograma
            ORDER BY mate.MATE_NOMBRE, pema.PEMA_PERIODO, grup.GRUP_NOMBRE
            """, nativeQuery = true)
    List<MateriaListadoProjection> findTransversalesByCoordinacionAndPrograma(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idPrograma") Long idPrograma);




    @Query(value = """
            SELECT
                MATE.MATE_CODIGOMATERIA AS codigoMateria,
                MATE.MATE_NOMBRE AS label
            FROM ACADEMICO.MATERIA MATE
            WHERE MATE.MATE_NOMBRE IS NOT NULL
            ORDER BY MATE.MATE_NOMBRE
            """, nativeQuery = true)
    List<MateriaCatalogoAdministracionProjection> findAdministrationOptions();



}
