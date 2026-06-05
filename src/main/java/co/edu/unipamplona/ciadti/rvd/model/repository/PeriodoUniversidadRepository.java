package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

public interface PeriodoUniversidadRepository
        extends JpaRepository<PeriodoUniversidadEntity, Long> {

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
            ORDER BY PEUN.PEUN_ANO DESC, PEUN.PEUN_PERIODO
            """, nativeQuery = true)
    List<PeriodoUniversidadEntity> findAllUniversityPeriods();
}
