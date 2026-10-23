package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TituloDocenteSniesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PublicAcademicTitleProjection;

public interface TituloDocenteSniesRepository
        extends JpaRepository<
                TituloDocenteSniesEntity,
                Long
        > {

    @Query("""
            SELECT titulo
            FROM TituloDocenteSniesEntity titulo
            ORDER BY
                titulo.nivel,
                titulo.descripcion
            """)
    List<TituloDocenteSniesEntity> findAcademicLevels();


    @Query(value = """
            SELECT
                TITU.TITU_ID AS id,

                TITU.TITD_ID AS idNivelAcademico,

                TRIM(
                    NVL(
                        TITU.TITU_DESCRIPCIONMASCULINA,
                        TITU.TITU_DESCRIPCIONFEMENINA
                    )
                ) AS nombre

            FROM GENERAL.TITULOGENERAL TITU

            WHERE TITU.TITD_ID = :idNivelAcademico

              AND NVL(
                    TITU.TITU_DESCRIPCIONMASCULINA,
                    TITU.TITU_DESCRIPCIONFEMENINA
                  ) IS NOT NULL

              AND (
                    UPPER(
                        NVL(
                            TITU.TITU_DESCRIPCIONMASCULINA,
                            TITU.TITU_DESCRIPCIONFEMENINA
                        )
                    )
                    LIKE
                    '%' || UPPER(:query) || '%'

                    OR

                    UPPER(
                        NVL(
                            TITU.TITU_DESCRIPCIONFEMENINA,
                            TITU.TITU_DESCRIPCIONMASCULINA
                        )
                    )
                    LIKE
                    '%' || UPPER(:query) || '%'
                  )

            ORDER BY
                UPPER(
                    NVL(
                        TITU.TITU_DESCRIPCIONMASCULINA,
                        TITU.TITU_DESCRIPCIONFEMENINA
                    )
                )

            FETCH FIRST 50 ROWS ONLY
            """,
            nativeQuery = true)
    List<PublicAcademicTitleProjection>
    findAcademicTitles(
            @Param("idNivelAcademico")
            Long idNivelAcademico,

            @Param("query")
            String query
    );
}