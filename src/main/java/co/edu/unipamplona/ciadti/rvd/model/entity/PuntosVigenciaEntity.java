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
    name = "PuntosVigenciaEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_PUNTOSVIGENCIA",
    parameters = {
        @StoredProcedureParameter(
            name = "P_PUVI_ID",
            mode = ParameterMode.IN,
            type = Long.class
        ),
        @StoredProcedureParameter(
            name = "P_PUVI_REGISTRADOPOR",
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
@Table(name = "PUNTOSVIGENCIA", schema = "RVD")
public class PuntosVigenciaEntity
        implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUVI_ID", nullable = false)
    private Long id;

    @Column(name = "PUVI_ANIO", nullable = false)
    private Long anio;

    @Column(name = "PUVI_VALORPUNTO")
    private String valorPunto;

    @Column(name = "PUVI_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PUVI_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone()
            throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PuntosVigenciaEntity{" +
                "id=" + id +
                ", anio=" + anio +
                ", valorPunto='" + valorPunto + '\'' +
                ", registradoPor='" + registradoPor + '\'' +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}