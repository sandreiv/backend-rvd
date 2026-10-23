package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "PTDIDOCUMENTO",
        schema = "RVD"
)
public class PtdiDocumentoEntity {

    @Id
    @Column(
            name = "UNIQUEID",
            nullable = false,
            length = 36
    )
    private String uniqueId;

    @Column(
            name = "PTDI_ID",
            nullable = false
    )
    private Long idPlanTrabajoDigital;

    @Lob
    @Column(
            name = "PTDI_ADJUNTO",
            columnDefinition = "CLOB"
    )
    private String adjunto;

    @Column(
            name = "PTDI_FECHACAMBIO",
            nullable = false
    )
    private Date fechaCambio;
}