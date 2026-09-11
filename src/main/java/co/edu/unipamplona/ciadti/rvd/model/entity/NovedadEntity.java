package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "NovedadEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_NOVEDADES",
    parameters = {
        @StoredProcedureParameter(
            name = "P_NOVE_ID",
            mode = ParameterMode.IN,
            type = Long.class
        ),
        @StoredProcedureParameter(
            name = "P_NOVE_REGISTRADOPOR",
            mode = ParameterMode.IN,
            type = String.class
        ),
        @StoredProcedureParameter(
            name = "P_EXITO",
            mode = ParameterMode.OUT,
            type = BigDecimal.class
        )
    }
)
@Getter
@Setter
@Entity
@Table(name = "NOVEDADES", schema = "RVD")
public class NovedadEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOVE_ID", nullable = false)
    private Long id;

    @Column(name = "NOVE_TIPO")
    private String tipo;

    @Column(name = "NOVE_DESCRIPCION")
    private String descripcion;

    @Column(name = "NOVE_ACCION")
    private String accion;

    @Column(name = "NOVE_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "NOVE_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "NovedadEntity{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", accion='" + accion + '\'' +
                ", registradoPor='" + registradoPor + '\'' +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}