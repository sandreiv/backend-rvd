/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
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
    name = "ConvocatoriaProyectosEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_CONVOCATORIAPROYECTOS",
    parameters = {
        @StoredProcedureParameter(name = "P_COPR_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COPR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "CONVOCATORIAPROYECTOS", schema = "RVD")
public class ConvocatoriaProyectosEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COPR_ID", nullable = false)
    private Long id;

    @Column(name = "COPR_NOMBRE")
    private String nombre;

    @Column(name = "COPR_DESCRIPCION")
    private String descripcion;

    @Column(name = "COPR_CODIGO")
    private String codigo;

    @Column(name = "COPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COPR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ConvocatoriaProyectosEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", codigo=" + codigo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
