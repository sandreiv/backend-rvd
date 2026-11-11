/**
 * Aplicación: rvd
 * Archivo: DocumentoVinculacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENTOVINCULACION", schema = "TALENTOV3")
public class DocumentoVinculacionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOVI_ID", nullable = false)
    private Long id;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "CONT_ID")
    private Long idContrato;

    @Column(name = "DOVI_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "DOVI_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORG_ID", insertable = false, updatable = false)
    private NormaGeneralEntity normaGeneral;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONT_ID", insertable = false, updatable = false)
    private ContratoEntity contrato;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DocumentoVinculacionEntity{" +
                "id=" + id +
                ", idNormaGeneral=" + idNormaGeneral +
                ", idContrato=" + idContrato +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
