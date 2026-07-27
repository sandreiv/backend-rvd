/**
 * Aplicación: rvd
 * Archivo: ProyectosEntity.java
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
    name = "ProyectosEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_PROYECTOS",
    parameters = {
        @StoredProcedureParameter(name = "P_PROY_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_PROY_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "PROYECTOS", schema = "RVD")
public class ProyectosEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROY_ID", nullable = false)
    private Long id;

    @Column(name = "COPR_ID")
    private Long idConvocatoriaProyectos;

    @Column(name = "PROY_IDPROYECTO")
    private Long idProyectoPadre;

    @Column(name = "TIPR_ID")
    private Long idTipoProyecto;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "PROY_MONTO")
    private String monto;

    @Column(name = "PROY_NOMBRE")
    private String nombre;

    @Column(name = "PROY_DESCRIPCION")
    private String descripcion;

    @Column(name = "PROY_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "PROY_FECHAFIN")
    private Date fechaFin;

    @Column(name = "PROY_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PROY_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPR_ID", insertable = false, updatable = false)
    private ConvocatoriaProyectosEntity convocatoriaProyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROY_IDPROYECTO", referencedColumnName = "PROY_ID", insertable = false, updatable = false)
    private ProyectosEntity proyectoPadre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPR_ID", insertable = false, updatable = false)
    private TipoProyectoEntity tipoProyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ProyectosEntity{" +
                "id=" + id +
                ", idConvocatoriaProyectos=" + idConvocatoriaProyectos +
                ", idProyectoPadre=" + idProyectoPadre +
                ", idTipoProyecto=" + idTipoProyecto +
                ", idCoordinacion=" + idCoordinacion +
                ", monto=" + monto +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
