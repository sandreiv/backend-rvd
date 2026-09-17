package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(
    name = "DetalleNovedadCargaDocenteEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_DETALLENOVEDADCARGADOCENTE",
    parameters = {
        @StoredProcedureParameter(name = "P_DNCD_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_DNCD_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "DETALLENOVEDADCARGADOCENTE", schema = "RVD")
public class DetalleNovedadCargaDocenteEntity implements Serializable, Cloneable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DNCD_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID")
    private Long idNovedadCargaDocente;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "GRUP_ID")
    private Long idGrupo;

    @Column(name = "TIAC_ID")
    private Long idTipoActividad;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "DNCD_HORAS")
    private String horas;

    @Column(name = "DNCD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "DNCD_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private NovedadCargaDocenteEntity novedadCargaDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROG_ID", insertable = false, updatable = false)
    private ProgramaEntity programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRUP_ID", insertable = false, updatable = false)
    private GrupoEntity grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private TipoActividadesEntity tipoActividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private CentroCostoEntity centroCosto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DetalleCargaDocenteEntity{" +
                "id=" + id +
                ", idNovedadCargaDocente=" + idNovedadCargaDocente +
                ", idPrograma=" + idPrograma +
                ", idGrupo=" + idGrupo +
                ", idTipoActividad=" + idTipoActividad +
                ", idCentroCosto=" + idCentroCosto +
                ", horas=" + horas +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
