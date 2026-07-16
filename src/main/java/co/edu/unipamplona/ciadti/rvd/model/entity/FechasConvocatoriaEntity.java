/**
 * Aplicación: rvd
 * Archivo: FechasConvocatoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.ParameterMode;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "FechasConvocatoriaEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_FECHASCONVOCATORIA",
    parameters = {
        @StoredProcedureParameter(name = "P_FECO_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_FECO_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "FECHASCONVOCATORIA", schema = "RVD")
public class FechasConvocatoriaEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FECO_ID", nullable = false)
    private Long id;

    @Column(name = "COTC_ID")
    private Long idConvocatoriaTipoContratacion;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "FECO_VACACIONES")
    private String vacaciones;

    @Column(name = "FECO_SEMANAS")
    private String semanas;

    @Column(name = "FECO_ONCEMESES")
    private String onceMeses;
    /*@Column(name = "TIAC_ID", nullable = false)
    private Long idTipoActividades;*/

    @Column(name = "FECO_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "FECO_FECHAFIN")
    private Date fechaFin;

    @Column(name = "FECO_CODIGO")
    private String codigo;

    @Column(name = "FECO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "FECO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COTC_ID", insertable = false, updatable = false)
    private ConvocatoriaTipoContratacionEntity convocatoriaTipoContratacion;

    /*@OneToMany
    @JoinColumn(name = "TIAC_ID",  insertable = false, updatable = false)
    private List<TipoActividadesEntity> tipoActividades;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private ConvocatoriaEntity convocatoria;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FechasConvocatoriaEntity{" +
                "id=" + id +
                ", idConvocatoriaTipoContratacion=" + idConvocatoriaTipoContratacion +
                ", idConvocatoria=" + idConvocatoria +
                ", vacaciones=" + vacaciones +
                ", onceMeses=" + onceMeses +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", codigo=" + codigo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
