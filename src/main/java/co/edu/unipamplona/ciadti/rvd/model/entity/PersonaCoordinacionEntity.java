/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "PersonaCoordinacionEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_PERSONACOORDINACION",
    parameters = {
        @StoredProcedureParameter(name = "P_PEGE_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COOR_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COOR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)

@Getter
@Setter
@Entity
@IdClass(PersonaCoordinacionEntityId.class)
@Table(name = "PERSONACOORDINACION", schema = "RVD")
public class PersonaCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Id
    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "PECO_ESTADO")
    private Long estado;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PersonaCoordinacionEntity{" +
                "idPersonaGeneral=" + idPersonaGeneral +
                ", idCoordinacion=" + idCoordinacion +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
