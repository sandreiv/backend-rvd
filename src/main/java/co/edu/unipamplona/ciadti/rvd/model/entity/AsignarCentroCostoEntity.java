/**
 * Aplicación: rvd
 * Archivo: AsignarCentroCostoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

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
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "AsignarCentroCostoEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_ASIGNARCENTROCOSTO",
    parameters = {
        @StoredProcedureParameter(name = "P_ASCC_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COOR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)

@Getter
@Setter
@Entity
@Table(name = "ASIGNARCENTROCOSTO", schema = "RVD")
public class AsignarCentroCostoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASCC_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "ASCC_ESTADO")
    private Long estado;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private CentroCostoEntity centroCosto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "AsignarCentroCostoEntity{" +
                "id=" + id +
                ", idCoordinacion=" + idCoordinacion +
                ", idCentroCosto=" + idCentroCosto +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
