/**
 * Aplicación: rvd
 * Archivo: ObservacionCargaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/08/2026
 */
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
    name = "ObservacionCargaEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_OBSERVACIONCARGA",
    parameters = {
        @StoredProcedureParameter(
            name = "P_OBCA_ID",
            mode = ParameterMode.IN,
            type = Long.class
        ),
        @StoredProcedureParameter(
            name = "P_OBCA_REGISTRADOPOR",
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
@Table(name = "OBSERVACIONCARGA", schema = "RVD")
public class ObservacionCargaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBCA_ID", nullable = false)
    private Long id;

    @Column(name = "CARG_ID")
    private Long idCarga;

    @Column(name = "PEGE_IDREGISTRA")
    private Long idPersonaGeneralRegistra;

    @Column(name = "OBCA_PEGEROL")
    private String rolPersonaGeneralRegistra;

    @Column(name = "OBSE_TEXTO")
    private String texto;

    @Column(name = "OBSE_FECHA")
    private Date fecha;

    @Column(name = "OBCA_VISTO")
    private Integer visto;

    @Column(name = "OBCA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "OBCA_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "CARG_ID",
        insertable = false,
        updatable = false
    )
    private CargaEntity carga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "PEGE_IDREGISTRA",
        insertable = false,
        updatable = false
    )
    private PersonaGeneralEntity personaRegistra;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ObservacionCargaEntity{" +
                "id=" + id +
                ", idCarga=" + idCarga +
                ", idPersonaGeneralRegistra=" + idPersonaGeneralRegistra +
                ", rolPersonaGeneralRegistra=" + rolPersonaGeneralRegistra +
                ", texto=" + texto +
                ", fecha=" + fecha +
                ", visto=" + visto +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}

/* 25/08/2026 @:Daniel Arias */