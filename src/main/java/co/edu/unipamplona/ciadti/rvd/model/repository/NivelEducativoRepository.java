package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;

public interface NivelEducativoRepository
        extends JpaRepository<NivelEducativoEntity, Long> {

    @Query(value = """
            SELECT
                NIED.NIED_ID,
                NIED.NIED_DESCRIPCION,
                NIED.NIED_FECHACAMBIO,
                NIED.NIED_REGISTRADOPOR,
                NIED.NIED_PARA_IES,
                NIED.NIED_OBSERVACION
            FROM ACADEMICO.NIVELEDUCATIVO NIED
            ORDER BY NIED.NIED_DESCRIPCION
            """, nativeQuery = true)
    List<NivelEducativoEntity> findAllEducationalLevels();
}
