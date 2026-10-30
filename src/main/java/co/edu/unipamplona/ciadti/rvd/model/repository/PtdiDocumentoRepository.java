package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    @Modifying
    @Query(value = """
            UPDATE RVD.PTDIDOCUMENTO PTDI
            SET PTDI.PTDI_ESTADO = '1'
            WHERE PTDI.UNIQUEID IN (:idsAdjuntos)
            """, nativeQuery = true)
    int updateStatePtdAttachments(
        @Param("idsAdjuntos") List<String> idsAdjuntos
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