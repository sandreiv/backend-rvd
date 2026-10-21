/**
 * Aplicación: rvd
 * Archivo: PresupuestoEgresoInversionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/10/2026
 * Modificaciones:
 * 19/10/2026 - Andrés Hernández - Creación inicial
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
@Table(name = "PRESUPUESTOEGRESOINVERSION", schema = "TESORERIAV3")
public class PresupuestoEgresoInversionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREI_ID", nullable = false)
    private Long id;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "FUFI_ID")
    private Long idFuenteFinanciacion;

    @Column(name = "RUVI_ID")
    private Long idRubroVigencia;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "ESPR_ID")
    private Long idEspr;

    @Column(name = "PECO_IDINICIAL")
    private String idInicial;

    @Column(name = "PECO_IDFINAL")
    private String idFinal;

    @Column(name = "UNID_IDSEDE")
    private Long idUnidadSede;

    @Column(name = "PREI_VALORINICIAL")
    private BigDecimal valorInicial;

    @Column(name = "PREI_VALORDEPENDENCIA")
    private BigDecimal valorDependencia;

    @Column(name = "PREI_VALORAPROBADO")
    private BigDecimal valorAprobado;

    @Column(name = "PREI_VALORACTUAL")
    private BigDecimal valorActual;

    @Column(name = "PREI_TOTALCDP")
    private BigDecimal totalCdp;

    @Column(name = "PREI_TOTALREGISTRADO")
    private BigDecimal totalRegistrado;

    @Column(name = "PREI_TOTALCAUSADO")
    private BigDecimal totalCausado;

    @Column(name = "PREI_TOTALEGRESADO")
    private BigDecimal totalEgresado;

    @Column(name = "PREI_PORCENTAJEGASTO")
    private String porcentajeGasto;

    @Column(name = "PREI_FECHAAPROBACION")
    private Date fechaAprobacion;

    @Column(name = "PREI_CONDICIONADO")
    private String condicionado;

    @Column(name = "PREI_CONTABILIZACION")
    private String contabilizacion;

    @Column(name = "PREI_PACDEFINIDO")
    private String pacDefinido;

    @Column(name = "PREI_FECHACONDICIONADO")
    private Date fehaCondicionado;

    @Column(name = "PREI_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "PREI_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_ID", insertable = false, updatable = false)
    private UnidadEntity unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private CentroCostoEntity centroCosto;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FUFI_ID", insertable = false, updatable = false)
    private FuenteFinanciacionEntity fuenteFinanciacion;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUVI_ID", insertable = false, updatable = false)
    private RubroVigenciaEntity rubroVigencia;

    /* RELACIONES ADICIONALES (falta colocar el nombre correcto de la entidad)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORG_ID", insertable = false, updatable = false)
    private NormaGeneralEntity normaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESPR_ID", insertable = false, updatable = false)
    private EsprEntity espr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PECO_IDINICIAL", insertable = false, updatable = false)
    private PeriodoContableEntity periodoContableIdInicial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PECO_IDFINAL", insertable = false, updatable = false)
    private PeriodoContableEntity periodoContableIdFinal;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_IDSEDE", insertable = false, updatable = false)
    private UnidadEntity unidadSede;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PresupuestoEgresoInversionEntity{" +
                "id=" + id +
                ", idUnidad=" + idUnidad +
                ", idCentroCosto=" + idCentroCosto +
                ", idFuenteFinanciacion=" + idFuenteFinanciacion +
                ", idRubroVigencia=" + idRubroVigencia +
                ", idNormaGeneral=" + idNormaGeneral +
                ", idEspr=" + idEspr +
                ", idInicial=" + idInicial +
                ", idFinal=" + idFinal +
                ", idUnidadSede=" + idUnidadSede +
                ", valorInicial=" + valorInicial +
                ", valorDependencia=" + valorDependencia +
                ", valorAprobado=" + valorAprobado +
                ", valorActual=" + valorActual +
                ", totalCdp=" + totalCdp +
                ", totalRegistrado=" + totalRegistrado +
                ", totalCausado=" + totalCausado +
                ", totalEgresado=" + totalEgresado +
                ", porcentajeGasto=" + porcentajeGasto +
                ", fechaAprobacion=" + fechaAprobacion +
                ", condicionado=" + condicionado +
                ", contabilizacion=" + contabilizacion +
                ", pacDefinido=" + pacDefinido +
                ", fehaCondicionado=" + fehaCondicionado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
