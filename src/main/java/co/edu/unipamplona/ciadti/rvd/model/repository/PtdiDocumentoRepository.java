package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.PtdiDocumentoEntity;

public interface PtdiDocumentoRepository
        extends JpaRepository<
                PtdiDocumentoEntity,
                String> {

    List<PtdiDocumentoEntity>
            findByIdPlanTrabajoDigital(
                    Long idPlanTrabajoDigital
            );

    Optional<PtdiDocumentoEntity>
            findFirstByIdPlanTrabajoDigitalOrderByFechaCambioDesc(
                    Long idPlanTrabajoDigital
            );

    List<PtdiDocumentoEntity>
            findByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNotNullOrderByFechaCambioDesc(
                    Long idPlanTrabajoDigital
            );
    
    Optional<PtdiDocumentoEntity>
            findFirstByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNullOrderByFechaCambioDesc(
                    Long idPlanTrabajoDigital
            );                    
}