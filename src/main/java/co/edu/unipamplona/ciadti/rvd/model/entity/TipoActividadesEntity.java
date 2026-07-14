/**
 * Aplicación: rvd
 * Archivo: TipoActividadesEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "TipoActividadesEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_TIPOACTIVIDADES",
    parameters = {
        @StoredProcedureParameter(name = "P_TIAC_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_TIAC_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)

@Getter
@Setter
@Entity
@Table(name = "TIPOACTIVIDADES", schema = "RVD")
public class TipoActividadesEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIAC_ID", nullable = false)
    private Long id;

    @Column(name = "TIAC_IDPADRE")
    private Long idPadre;

    @Column(name = "TIAC_NOMBRE")
    private String nombre;

    @Column(name = "TIAC_DESCRIPCION")
    private String descripcion;

    @Column(name = "TIAC_ORDEN")
    private String orden;

    @Column(name = "TIAC_ESTADO")
    private String estado;

    @Column(name = "TIAC_CODIGO")
    private String codigo;

    @Column(name = "TIAC_COMPONENTE")
    private String componente;

    @Column(name = "TIAC_CLASE")
    private String clase;

    @Column(name = "TIAC_MINIMOHORAS")
    private String minimoHoras;

    @Column(name = "TIAC_MAXIMOHORAS")
    private String maximoHoras;

    @Column(name = "TIAC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIAC_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoActividadesEntity{" +
                "id=" + id +
                ", idPadre=" + idPadre +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", orden=" + orden +
                ", estado=" + estado +
                ", codigo=" + codigo +
                ", componente=" + componente +
                ", clase=" + clase +
                ", minimoHoras=" + minimoHoras +
                ", maximoHoras=" + maximoHoras +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
