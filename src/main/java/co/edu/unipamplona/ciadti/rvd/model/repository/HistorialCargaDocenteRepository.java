package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.HistorialCargaDocenteEntity;

public interface HistorialCargaDocenteRepository extends JpaRepository<HistorialCargaDocenteEntity, Long>{
    
    List<HistorialCargaDocenteEntity> findByIdCargaDocente(Long idCargaDocente);

    @Procedure(name = "HistorialCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
        @Param("P_HICD_ID") Long id,
        @Param("P_HICD_REGISTRADOPOR") String registradoPor
    );
}
