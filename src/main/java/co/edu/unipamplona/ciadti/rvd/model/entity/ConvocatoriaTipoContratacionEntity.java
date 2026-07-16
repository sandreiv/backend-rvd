/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaTipoContratacionEntity.java
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

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.ParameterMode;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "ConvocatoriaTipoContratacionEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_CONVOCATORIATIPOCONTRATACION",
    parameters = {
        @StoredProcedureParameter(name = "P_COTC_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_COTC_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "CONVOCATORIATIPOCONTRATACION", schema = "RVD")
public class ConvocatoriaTipoContratacionEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COTC_ID", nullable = false)
    private Long id;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "MOCO_ID", nullable = false)
    private Long idModalidadContratacion;

    @Column(name = "COTC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COTC_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private ConvocatoriaEntity convocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOCO_ID", insertable = false, updatable = false)
    private ModalidadContratacionEntity modalidadContratacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ConvocatoriaTipoContratacionEntity{" +
                "id=" + id +
                ", idConvocatoria=" + idConvocatoria +
                ", idModalidadContratacion=" + idModalidadContratacion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
