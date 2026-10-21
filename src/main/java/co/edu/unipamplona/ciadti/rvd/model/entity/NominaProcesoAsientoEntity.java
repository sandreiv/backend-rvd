/**
 * Aplicación: rvd
 * Archivo: NominaProcesoAsientoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Andrés Hernández - Creación inicial
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
@Table(name = "NOMINAPROCESOASIENTO", schema = "TESORERIAV3")
public class NominaProcesoAsientoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOPA_ID", nullable = false)
    private Long id;

    @Column(name = "NOMI_ID")
    private Long idNomina;

    @Column(name = "PRAS_ID")
    private String idPras;

    @Column(name = "TRLI_ID")
    private String idTrli;

    @Column(name = "EVLI_ID")
    private Long idEvli;

    @Column(name = "NOPA_CONTABILIZACION")
    private String contabilizacion;

    @Column(name = "NOPA_AFECTACONTABILIDAD")
    private String afectaContabilidad;

    @Column(name = "NOPA_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "NOPA_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES (falta colocar el nombre correcto de la entidad)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOMI_ID", insertable = false, updatable = false)
    private NominaEntity nomina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRAS_ID", insertable = false, updatable = false)
    private PrasEntity pras;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRLI_ID", insertable = false, updatable = false)
    private TrliEntity trli;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVLI_ID", insertable = false, updatable = false)
    private EvliEntity evli;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "NominaProcesoAsientoEntity{" +
                "id=" + id +
                ", idNomina=" + idNomina +
                ", idPras=" + idPras +
                ", idTrli=" + idTrli +
                ", idEvli=" + idEvli +
                ", contabilizacion=" + contabilizacion +
                ", afectaContabilidad=" + afectaContabilidad +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
