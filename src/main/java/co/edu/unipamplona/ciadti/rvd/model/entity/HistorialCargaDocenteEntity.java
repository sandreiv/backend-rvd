package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
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
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "HistorialCargaDocenteEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_HISTORIALCARGADOCENTE",
    parameters = {
        @StoredProcedureParameter(name = "P_HICD_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_HICD_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "HISTORIALCARGADOCENTE", schema = "RVD")
public class HistorialCargaDocenteEntity implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HICD_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID", nullable = false)
    private Long idCargaDocente;

    @Column(name = "HICD_ESTADO")
    private String estado;

    @Column(name = "HICD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "HICD_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocente;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "HistorialCargaDocenteEntity{" +
                "id=" + id +
                ", idCargaDocente=" + idCargaDocente +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
