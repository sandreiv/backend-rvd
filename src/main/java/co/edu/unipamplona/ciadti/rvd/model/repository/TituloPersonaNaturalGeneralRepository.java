package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TituloPersonaNaturalGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PublicAcademicTrainingProjection;

public interface TituloPersonaNaturalGeneralRepository
        extends JpaRepository<
                TituloPersonaNaturalGeneralEntity,
                Long
        > {

    @Query(
        value = """
            SELECT COUNT(1)
            FROM GENERAL.TITULOGENERAL
            WHERE TITU_ID = :idTitulo
              AND TITD_ID = :idNivelAcademico
            """,
        nativeQuery = true
    )
    Long countTitleByIdAndAcademicLevel(
            @Param("idTitulo")
            Long idTitulo,

            @Param("idNivelAcademico")
            Long idNivelAcademico
    );


    @Modifying
    @Query(
        value = """
            INSERT INTO GENERAL.TITULOPERSONANATURALGENERAL (
                PEGE_ID,
                TITU_ID,
                TIPN_ESTABLECIMIENTO,
                TIPN_REGISTRADOPOR,
                TIPN_FECHACAMBIO,
                PAGE_ID,
                TIPN_FECHAOBTENCION
            )
            VALUES (
                :idPersonaGeneral,
                :idTitulo,
                :institucion,
                :registradoPor,
                SYSDATE,
                :idPais,
                :fechaGrado
            )
            """,
        nativeQuery = true
    )
    int insertAcademicTraining(
            @Param("idPersonaGeneral")
            Long idPersonaGeneral,

            @Param("idTitulo")
            Long idTitulo,

            @Param("institucion")
            String institucion,

            @Param("registradoPor")
            String registradoPor,

            @Param("idPais")
            String idPais,

            @Param("fechaGrado")
            Date fechaGrado
    );

    @Query(
        value = """
            SELECT
                TPNG.TPNG_ID AS id,

                TG.TITD_ID AS idNivelAcademico,

                TD.TITD_DESCRIPCION AS nivelAcademico,

                TPNG.TITU_ID AS idTitulo,

                TRIM(
                    NVL(
                        TG.TITU_DESCRIPCIONMASCULINA,
                        TG.TITU_DESCRIPCIONFEMENINA
                    )
                ) AS titulo,

                TPNG.TIPN_ESTABLECIMIENTO AS institucion,

                TPNG.TIPN_FECHAOBTENCION AS fechaGrado,

                TPNG.PAGE_ID AS idPais,

                PG.PAGE_NOMBRE AS pais

            FROM GENERAL.TITULOPERSONANATURALGENERAL TPNG

            INNER JOIN GENERAL.TITULOGENERAL TG
                    ON TG.TITU_ID = TPNG.TITU_ID

            INNER JOIN ACADEMICO.TITULODOCENTESNIES TD
                    ON TD.TITD_ID = TG.TITD_ID

            INNER JOIN GENERAL.PAISGENERAL PG
                    ON PG.PAGE_ID = TPNG.PAGE_ID

            WHERE TPNG.PEGE_ID = :idPersonaGeneral

            ORDER BY
                TPNG.TIPN_FECHACAMBIO DESC,
                TPNG.TPNG_ID DESC

            FETCH FIRST 1 ROW ONLY
            """,
        nativeQuery = true
    )
    Optional<PublicAcademicTrainingProjection>
    findLatestAcademicTraining(
            @Param("idPersonaGeneral")
            Long idPersonaGeneral
    );
}