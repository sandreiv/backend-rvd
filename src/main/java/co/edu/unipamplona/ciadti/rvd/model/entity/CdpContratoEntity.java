/**
 * Aplicación: rvd
 * Archivo: CdpContratoEntity.java
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
@Table(name = "CDPCONTRATO", schema = "CONTRATOS")
public class CdpContratoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDCO_ID", nullable = false)
    private Long id;

    @Column(name = "CONT_ID")
    private Long idContrato;

    @Column(name = "CDP_ID")
    private Long idCdp;

    @Column(name = "CDCO_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "CDCO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONT_ID", insertable = false, updatable = false)
    private ContratoEntity contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDP_ID", insertable = false, updatable = false)
    private CdpEntity cdp;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CdpContratoEntity{" +
                "id=" + id +
                ", idContrato=" + idContrato +
                ", idCdp=" + idCdp +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
