/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpTesoreriaV3Entity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.math.BigDecimal;
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
@Table(name = "SOLICITUDCDP", schema = "TESORERIAV3")
public class SolicitudCdpTesoreriaV3Entity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCDP_ID", nullable = false)
    private Long id;

    @Column(name = "TRLU_ID")
    private Long idTrlu;

    @Column(name = "ENCA_ID")
    private Long idEnca;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "SCDP_NUMERO")
    private Long numero;

    @Column(name = "SCDP_CONCEPTO")
    private String concepto;

    @Column(name = "SCDP_FECHASOLICITUD")
    private Date fechaSolicitud;

    @Column(name = "SCDP_MONTO")
    private BigDecimal monto;

    @Column(name = "SCDP_ESTADO")
    private String estado;

    @Column(name = "SCDP_VIGENCIAFUTURA")
    private String vigenciaFutura;

    @Column(name = "SCDP_JUSTIFICACIONANULACION")
    private String justificacionAnulacion;

    @Column(name = "SCDP_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "SCDP_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES (falta colocar el nombre correcto de la entidad)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRLU_ID", insertable = false, updatable = false)
    private TrluEntity trlu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENCA_ID", insertable = false, updatable = false)
    private EncaEntity enca;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_ID", insertable = false, updatable = false)
    private UnidadEntity unidad;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "SolicitudCdpTesoreriaV3Entity{" +
                "id=" + id +
                ", idTrlu=" + idTrlu +
                ", idEnca=" + idEnca +
                ", idUnidad=" + idUnidad +
                ", numero=" + numero +
                ", concepto=" + concepto +
                ", fechaSolicitud=" + fechaSolicitud +
                ", monto=" + monto +
                ", estado=" + estado +
                ", vigenciaFutura=" + vigenciaFutura +
                ", justificacionAnulacion=" + justificacionAnulacion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
