package co.edu.unipamplona.ciadti.rvd.model.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CENTROCOSTO", schema = "CONTABLEV3")
public class CentroCostoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CECO_ID", nullable = false)
    private Long id;

    @Column(name = "GRCC_ID")
    private Long idGrupoCentroCosto;

    @Column(name = "CLCC_ID")
    private Long idClaseCentroCosto;

    @Column(name = "PEGE_IDRESPONSABLE")
    private Long idResponsable;

    @Column(name = "CECO_IDPADRE")
    private Long idPadre;

    @Column(name = "CECO_ESTADO")
    private String estado;

    @Column(name = "CECO_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "CECO_CODIGO", nullable = false)
    private String codigo;

    @Column(name = "CECO_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "CECO_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    /*@OneToMany
    @JoinColumn(name = "GRCC_ID", insertable = false, updatable = false)
    private List<GrupoCentroCostoEntity> gruposCentroCosto;

    @OneToMany
    @JoinColumn(name = "CLCC_ID", insertable = false, updatable = false)
    private List<ClaseCentroCostoEntity> clasesCentroCosto;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_IDRESPONSABLE", insertable = false, updatable = false)
    private PersonaGeneralEntity responsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CECO_IDPADRE", referencedColumnName = "CECO_ID", insertable = false, updatable = false)
    private CentroCostoEntity padre;

    //private List<CentroCostoEntity> padres;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CentroCostoEntity{" +
                "id=" + id +
                ", idGrupoCentroCosto=" + idGrupoCentroCosto +
                ", idClaseCentroCosto=" + idClaseCentroCosto +
                ", idResponsable=" + idResponsable +
                ", idPadre=" + idPadre +
                ", estado=" + estado +
                ", descripcion=" + descripcion +
                ", codigo=" + codigo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}