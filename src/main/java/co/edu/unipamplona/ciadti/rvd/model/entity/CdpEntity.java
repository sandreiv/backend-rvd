/**
 * Aplicación: rvd
 * Archivo: CdpEntity.java
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
@Table(name = "CDP", schema = "TESORERIAV3")
public class CdpEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDP_ID", nullable = false)
    private Long id;

    @Column(name = "SCDP_ID")
    private Long idSolicitudCdp;

    @Column(name = "SOCB_ID")
    private Long idSolicitudCotizacionBase;

    @Column(name = "NOPA_ID")
    private Long idNominaProcesoAsiento;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "UNID_IDNUMERACION")
    private Long idUnidadNumeracion;

    @Column(name = "CDP_NUMERO")
    private String cdpNumero;

    @Column(name = "CDP_FECHAEXPEDICION")
    private Date fechaExpedicion;

    @Column(name = "CDP_CONCEPTO")
    private String concepto;

    @Column(name = "CDP_ESTADO")
    private String estado;

    @Column(name = "CDP_CONTABILIZACION")
    private String contabilizacion;

    @Column(name = "CDP_FECHAVENCE")
    private Date fechaVence;

    @Column(name = "CDP_VIGENCIAFUTURA")
    private String vigenciaFutura;

    @Column(name = "CDP_JUSTIFICACIONANULACION")
    private String justificacionAnulacion;

    @Column(name = "CDP_VIGENCIA")
    private String vigencia;

    @Column(name = "CDP_APLICAFIRMA")
    private String aplicaFirma;

    @Column(name = "CDP_VIENEDETALENTO")
    private String vieneDeTalento;

    @Column(name = "CDP_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "CDP_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCDP_ID", insertable = false, updatable = false)
    private SolicitudCdpTesoreriaV3Entity solicitudCdp;

    /* RELACIONES ADICIONALES 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCB_ID", insertable = false, updatable = false)
    private SolicitudCotizacionBaseEntity solicitudCotizacionBase;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOPA_ID", insertable = false, updatable = false)
    private NominaProcesoAsientoEntity nominaProcesoAsiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_ID", insertable = false, updatable = false)
    private UnidadEntity unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_IDNUMERACION", insertable = false, updatable = false)
    private UnidadEntity unidadNumeracion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CdpEntity{" +
                "id=" + id +
                ", idSolicitudCdp=" + idSolicitudCdp +
                ", idSolicitudCotizacionBase=" + idSolicitudCotizacionBase +
                ", idNominaProcesoAsiento=" + idNominaProcesoAsiento +
                ", idUnidad=" + idUnidad +
                ", idUnidadNumeracion=" + idUnidadNumeracion +
                ", cdpNumero=" + cdpNumero +
                ", fechaExpedicion=" + fechaExpedicion +
                ", concepto=" + concepto +
                ", estado=" + estado +
                ", contabilizacion=" + contabilizacion +
                ", fechaVence=" + fechaVence +
                ", vigenciaFutura=" + vigenciaFutura +
                ", justificacionAnulacion=" + justificacionAnulacion +
                ", vigencia=" + vigencia +
                ", aplicaFirma=" + aplicaFirma +
                ", vieneDeTalento=" + vieneDeTalento +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
