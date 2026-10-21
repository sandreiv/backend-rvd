/**
 * Aplicación: rvd
 * Archivo: CdpRubroEntity.java
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
@Table(name = "CDPRUBRO", schema = "TESORERIAV3")
public class CdpRubroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDRU_ID", nullable = false)
    private Long id;

    @Column(name = "CDP_ID")
    private Long idCdp;

    @Column(name = "PREI_ID")
    private Long idPresupuestoEgresoInversion;

    @Column(name = "DVFU_ID")
    private Long idDetalleVigenciaFutura;

    @Column(name = "CCPC_ID")
    private Long idCodigoCpc;

    @Column(name = "PRYI_ID")
    private Long idProyectoInversion;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "COVE_ID")
    private Long idConvenio;

    @Column(name = "CDRU_VALORINICIAL")
    private BigDecimal valorInicial;

    @Column(name = "CDRU_VALORACTUAL")
    private BigDecimal valorActual;

    @Column(name = "CDRU_TOTALREGISTRADO")
    private BigDecimal totalRegistrado;

    @Column(name = "CDRU_TOTALEGRESADO")
    private BigDecimal totalEgresado;

    @Column(name = "CDRU_TOTALCAUSADO")
    private BigDecimal totalCausado;

    @Column(name = "CDRU_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "CDRU_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDP_ID", insertable = false, updatable = false)
    private CdpEntity cdp;

    /* RELACIONES ADICIONALES 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREI_ID", insertable = false, updatable = false)
    private PresupuestoEgresoInversionEntity presupuestoEgresoInversion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DVFU_ID", insertable = false, updatable = false)
    private DetalleVigenciaFuturaEntity detalleVigenciaFutura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CCPC_ID", insertable = false, updatable = false)
    private CodigoCpcEntity codigoCpc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRYI_ID", insertable = false, updatable = false)
    private ProyectoInversionEntity proyectoInversion;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROG_ID", insertable = false, updatable = false)
    private ProgramaEntity programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_ID", insertable = false, updatable = false)
    private UnidadEntity unidad;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COVE_ID", insertable = false, updatable = false)
    private ConvenioEntity convenio;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CdpRubroEntity{" +
                "id=" + id +
                ", idCdp=" + idCdp +
                ", idPresupuestoEgresoInversion=" + idPresupuestoEgresoInversion +
                ", idDetalleVigenciaFutura=" + idDetalleVigenciaFutura +
                ", idCodigoCpc=" + idCodigoCpc +
                ", idProyectoInversion=" + idProyectoInversion +
                ", idPrograma=" + idPrograma +
                ", idUnidad=" + idUnidad +
                ", idConvenio=" + idConvenio +
                ", valorInicial=" + valorInicial +
                ", valorActual=" + valorActual +
                ", totalRegistrado=" + totalRegistrado +
                ", totalEgresado=" + totalEgresado +
                ", totalCausado=" + totalCausado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
