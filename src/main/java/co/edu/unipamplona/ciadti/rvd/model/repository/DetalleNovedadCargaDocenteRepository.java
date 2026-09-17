package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;

public interface DetalleNovedadCargaDocenteRepository
        extends JpaRepository<DetalleNovedadCargaDocenteEntity, Long> {

    List<DetalleNovedadCargaDocenteEntity> findByIdNovedadCargaDocente(
            Long idNovedadCargaDocente
    );

    @Procedure(name = "DetalleNovedadCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_DNCD_ID") Long id,
            @Param("P_DNCD_REGISTRADOPOR") String registradoPor
    );
}
