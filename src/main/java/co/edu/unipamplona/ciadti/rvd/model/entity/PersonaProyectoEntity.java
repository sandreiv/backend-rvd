/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoEntity.java
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
    name = "PersonaProyectoEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_PERSONAPROYECTO",
    parameters = {
        @StoredProcedureParameter(name = "P_PEPR_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_PEPR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "PERSONAPROYECTO", schema = "RVD")
public class PersonaProyectoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEPR_ID", nullable = false)
    private Long id;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "PROY_ID")
    private Long idProyecto;

    @Column(name = "TIAC_ID")
    private Long idTipoActividad;

    @Column(name = "PEGE_IDFUNCIONARIO")
    private Long idPersonaGeneralFuncionario;

    @Column(name = "PEPR_TIPO")
    private String tipo;

    @Column(name = "PEPR_HORAS")
    private String horas;

    @Column(name = "PEPR_OBSERVACION")
    private String observacion;

    @Column(name = "PEPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PEPR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROY_ID", insertable = false, updatable = false)
    private ProyectosEntity proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private TipoActividadesEntity tipoActividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_IDFUNCIONARIO", insertable = false, updatable = false)
    private PersonaGeneralEntity funcionario;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PersonaProyectoEntity{" +
                "id=" + id +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", idProyecto=" + idProyecto +
                ", idTipoActividad=" + idTipoActividad +
                ", idPersonaGeneralFuncionario=" + idPersonaGeneralFuncionario +
                ", tipo=" + tipo +
                ", horas=" + horas +
                ", observacion=" + observacion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
