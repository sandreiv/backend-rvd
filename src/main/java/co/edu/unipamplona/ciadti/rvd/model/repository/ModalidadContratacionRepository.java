package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadContratacionEntity;

public interface ModalidadContratacionRepository
        extends JpaRepository<ModalidadContratacionEntity, Long> {

    @Query(value = """
            SELECT
                MOCO.MOCO_ID,
                MOCO.MOCO_NOMBRE,
                MOCO.MOCO_DESCRIPCION,
                MOCO.MOCO_INSTRUCTIVO,
                MOCO.MOCO_ESTADO,
                MOCO.MOCO_SIGLA,
                MOCO.MOCO_REGISTRADOPOR,
                MOCO.MOCO_FECHACAMBIO,
                MOCO.MOCO_PORCENTAJEMAXIMOANTICIPO,
                MOCO.CLMO_ID,
                MOCO.MOCO_PORCENTAJEMINIMOANTICIPO
            FROM CONTRATOS.MODALIDADCONTRATACION MOCO
            ORDER BY MOCO.MOCO_NOMBRE
            """, nativeQuery = true)
    List<ModalidadContratacionEntity> findAllModalities();
}
