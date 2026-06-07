package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

public interface ConvocatoriaRepository extends JpaRepository<ConvocatoriaEntity, Long> {

    @Query(value = """
            SELECT DISTINCT
                CONV.CONV_ID,
                CONV.PEUN_ID,
                CONV.NIED_ID,
                CONV.PEGE_IDAUTORIZA,
                CONV.CONV_NOMBRE,
                CONV.CONV_DESCRIPCION,
                CONV.CONV_ESTADO,
                CONV.CONV_REGISTRADOPOR,
                CONV.CONV_FECHACAMBIO
            FROM RVD.CONVOCATORIA CONV
            INNER JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.CONV_ID = CONV.CONV_ID
            WHERE FECO.FECO_CODIGO = 'CNV'
            ORDER BY CONV.CONV_ID
            """, nativeQuery = true)
    List<ConvocatoriaEntity> findCallListWithDates();

    @Query(value = """
            SELECT
                FECO.FECO_ID,
                FECO.COTC_ID,
                FECO.CONV_ID,
                FECO.FECO_VACACIONES,
                FECO.FECO_SEMANAS,
                FECO.FECO_ONCEMESES,
                FECO.FECO_FECHAINICIO,
                FECO.FECO_FECHAFIN,
                FECO.FECO_CODIGO,
                FECO.FECO_REGISTRADOPOR,
                FECO.FECO_FECHACAMBIO
            FROM RVD.FECHASCONVOCATORIA FECO
            WHERE FECO.CONV_ID = :id
                AND FECO.FECO_CODIGO = 'CNV'
            """, nativeQuery = true)
    FechasConvocatoriaEntity findFechaCnvByConvocatoriaId(@Param("id") Long id);



    @Query(value = """
            SELECT
                CONV.CONV_ID,
                CONV.PEUN_ID,
                CONV.NIED_ID,
                CONV.PEGE_IDAUTORIZA,
                CONV.CONV_NOMBRE,
                CONV.CONV_DESCRIPCION,
                CONV.CONV_ESTADO,
                CONV.CONV_REGISTRADOPOR,
                CONV.CONV_FECHACAMBIO
            FROM RVD.CONVOCATORIA CONV
            WHERE CONV.CONV_ID = :id
            """, nativeQuery = true)

    Optional<ConvocatoriaEntity> findConvocatoriaByIdNative(@Param("id") Long id);

    @Query(value = """
            SELECT
                PEUN.PEUN_ID,
                PEUN.TPPA_ID,
                PEUN.PEUN_FECHAINICIO,
                PEUN.PEUN_FECHAFIN,
                PEUN.PEUN_ANO,
                PEUN.PEUN_PERIODO,
                PEUN.PEUN_FECHAINICIOCLASES,
                PEUN.PEUN_FECHAFINCLASES,
                PEUN.PEUN_CODIGOPERIODO,
                PEUN.PEUN_ACTUAL,
                PEUN.PEUN_REGISTRADOPOR,
                PEUN.PEUN_FECHACAMBIO
            FROM ACADEMICO.PERIODOUNIVERSIDAD PEUN
            INNER JOIN RVD.CONVOCATORIA CONV
                ON CONV.PEUN_ID = PEUN.PEUN_ID
            WHERE CONV.CONV_ID = :id
            """, nativeQuery = true)

    PeriodoUniversidadEntity findPeriodoEntityByConvocatoriaId(@Param("id") Long id);

    @Query(value = """
            SELECT
                NIED.NIED_ID,
                NIED.NIED_DESCRIPCION,
                NIED.NIED_FECHACAMBIO,
                NIED.NIED_REGISTRADOPOR,
                NIED.NIED_PARA_IES,
                NIED.NIED_OBSERVACION
            FROM ACADEMICO.NIVELEDUCATIVO NIED
            INNER JOIN RVD.CONVOCATORIA CONV
                ON CONV.NIED_ID = NIED.NIED_ID
            WHERE CONV.CONV_ID = :id
            """, nativeQuery = true)

    NivelEducativoEntity findNivelEntityByConvocatoriaId(@Param("id") Long id);

    @Query(value = """
            SELECT
                FECO.FECO_ID,
                FECO.COTC_ID,
                FECO.CONV_ID,
                FECO.FECO_VACACIONES,
                FECO.FECO_SEMANAS,
                FECO.FECO_ONCEMESES,
                FECO.FECO_FECHAINICIO,
                FECO.FECO_FECHAFIN,
                FECO.FECO_CODIGO,
                FECO.FECO_REGISTRADOPOR,
                FECO.FECO_FECHACAMBIO
            FROM RVD.FECHASCONVOCATORIA FECO
            WHERE FECO.CONV_ID = :id
                AND FECO.FECO_CODIGO IS NOT NULL
            ORDER BY FECO.FECO_ID
            """, nativeQuery = true)

    List<FechasConvocatoriaEntity> findFechasGeneralesByConvocatoriaId(@Param("id") Long id);

    @Query(value = """
            SELECT
                FECO.FECO_ID,
                FECO.COTC_ID,
                FECO.CONV_ID,
                FECO.FECO_VACACIONES,
                FECO.FECO_SEMANAS,
                FECO.FECO_ONCEMESES,
                FECO.FECO_FECHAINICIO,
                FECO.FECO_FECHAFIN,
                FECO.FECO_CODIGO,
                FECO.FECO_REGISTRADOPOR,
                FECO.FECO_FECHACAMBIO
            FROM RVD.FECHASCONVOCATORIA FECO
            WHERE FECO.CONV_ID = :id
                AND FECO.COTC_ID IS NOT NULL
            ORDER BY FECO.FECO_ID
            """, nativeQuery = true)
    List<FechasConvocatoriaEntity> findModalidadesFechasByConvocatoriaId(
            @Param("id") Long id);

    @Modifying
    @Query("""
            update ConvocatoriaEntity c set
            c.nombre = :nombre,
            c.descripcion = :descripcion,
            c.idPersonaGeneral = :idPersonaGeneral,
            c.idPeriodoUniversidad = :idPeriodoUniversidad,
            c.idNivelEducativo = :idNivelEducativo,
            c.fechaCambio = :fechaCambio
            where c.id = :id
            """)
    int update(
            @Param("nombre") String nombre,
            @Param("descripcion") String descripcion,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad,
            @Param("idNivelEducativo") Long idNivelEducativo,
            @Param("fechaCambio") Date fechaCambio,
            @Param("id") Long id);

    

    @Modifying
    @Query(value = """
            DELETE FROM RVD.CONVOCATORIA WHERE CONV_ID = :id
            """, nativeQuery = true)
    void deleteById(@Param("id") Long id);

}

