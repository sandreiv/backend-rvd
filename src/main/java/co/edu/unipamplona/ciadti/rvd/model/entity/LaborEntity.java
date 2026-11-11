/**
 * Aplicación: rvd
 * Archivo: LaborEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LABOR", schema = "TALENTOV3")
public class LaborEntity {
    
    @Id
    @Column(name = "LABO_ID", nullable = false)
    private Long id;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "NIJE_ID")
    private Long idNivelJerarquico;

    @Column(name = "ESAL_ID")
    private Long idEscalaSalarial;
    
    @Column(name = "NOSU_ID")
    private Long idNomenclaturaSuip;

    @Column(name = "TESU_ID")
    private Long idTipoEmpleoSuip;

    @Column(name = "NAES_ID")
    private Long idNaturalezaEmpleoSuip;

    @Column(name = "TIPL_ID")
    private Long idTipoDePlantaSuip;

    @Column(name = "NORG_IDINHABILITACION")
    private Long idNormaGeneralInhabilitacion;

    @Column(name = "SOLA_ID")
    private Long idSolicitudLabor;
    
    @Column(name = "LABO_DESCRIPCION")
    private String descripcion;

    @Column(name = "LABO_NOMBRE")
    private String nombre;

    @Column(name = "LABO_TIPO")
    private String tipo;

    @Column(name = "LABO_NUMEROEMPLEOS")
    private Long numeroEmpleos;

    @Column(name = "LABO_TIENEFUNCION")
    private String tieneFuncion;

    @Column(name = "LABO_DOCENTE")
    private String docente;

    @Column(name = "LABO_REPRESENTACION")
    private String representacion;

    @Column(name = "LABO_VALORGASTOSREP")
    private BigDecimal valorGastosRep;

    @Column(name = "LABO_TIENEPRIMATECNICA")
    private String tienePrimaTecnica;

    @Column(name = "LABO_CODIGO")
    private String codigo;

    @Column(name = "LABO_GRADO")
    private Long grado;

    @Column(name = "LABO_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "LABO_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORG_ID", insertable = false, updatable = false)
    private NormaGeneralEntity normaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NIJE_ID", insertable = false, updatable = false)
    private NivelJerarquicoEntity nivelJerarquico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESAL_ID", insertable = false, updatable = false)
    private EscalaSalarialEntity escalaSalarial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOSU_ID", insertable = false, updatable = false)
    private NomenclaturaSuipEntity nomenclaturaSuip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESU_ID", insertable = false, updatable = false)
    private TipoEmpleoSuipEntity tipoEmpleoSuip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NAES_ID", insertable = false, updatable = false)
    private NaturalezaEmpleoSuip naturalezaEmpleoSuip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPL_ID", insertable = false, updatable = false)
    private TipoDePlantaSuipEntity tipoDePlantaSuip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORG_IDINHABILITACION", insertable = false, updatable = false)
    private NormaGeneralEntity normaGeneralInhabilitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLA_ID", insertable = false, updatable = false)
    private SolicitudLaborEntuty solicitudLabor;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "LaborEntity{" +
                "id=" + id +
                ", idNormaGeneral=" + idNormaGeneral +
                ", idNivelJerarquico=" + idNivelJerarquico +
                ", idEscalaSalarial=" + idEscalaSalarial +
                ", idNomenclaturaSuip=" + idNomenclaturaSuip +
                ", idTipoEmpleoSuip=" + idTipoEmpleoSuip +
                ", idNaturalezaEmpleoSuip=" + idNaturalezaEmpleoSuip +
                ", idTipoDePlantaSuip=" + idTipoDePlantaSuip +
                ", idNormaGeneralInhabilitacion=" + idNormaGeneralInhabilitacion +
                ", idSolicitudLabor=" + idSolicitudLabor +
                ", descripcion=" + descripcion +
                ", nombre=" + nombre +
                ", tipo=" + tipo +
                ", numeroEmpleos=" + numeroEmpleos +
                ", tieneFuncion=" + tieneFuncion +
                ", docente=" + docente +
                ", representacion=" + representacion +
                ", valorGastosRep=" + valorGastosRep +
                ", tienePrimaTecnica=" + tienePrimaTecnica +
                ", codigo=" + codigo +
                ", grado=" + grado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
