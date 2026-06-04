/**
 * Aplicación: rvd
 * Archivo: PeriodoUniversidadEntity.java
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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PERIODOUNIVERSIDAD", schema = "ACADEMICO")
public class PeriodoUniversidadEntity implements Serializable, Cloneable{

    @Id
    @Column(name = "PEUN_ID", nullable = false)
    private Long id;

    @Column(name = "TPPA_ID", nullable = false)
    private Long idTipoPeriodoAcademico;

    @Column(name = "PEUN_FECHAINICIO", nullable = false)
    private Date fechaInicio;

    @Column(name = "PEUN_FECHAFIN", nullable = false)
    private Date fechaFin;

    @Column(name = "PEUN_ANO", nullable = false)
    private Long ano;

    @Column(name = "PEUN_PERIODO")
    private String periodo;

    @Column(name = "PEUN_FECHAINICIOCLASES")
    private Date fechaInicioClases;

    @Column(name = "PEUN_FECHAFINCLASES")
    private Date fechaFinClases;

    @Column(name = "PEUN_CODIGOPERIODO")
    private String codigoPeriodo;

    @Column(name = "PEUN_ACTUAL")
    private String actual;

    @Column(name = "PEUN_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PEUN_FECHACAMBIO")
    private Date fechaCambio;

    
    @OneToMany
    @JoinColumn(name = "TPPA_ID",  insertable = false, updatable = false)
    private List<TipoPeriodoAcademicoEntity> tipoPeriodoAcademico;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PeriodoUniversidadEntity{" +
                "id=" + id +
                ", idTipoPeriodoAcademico=" + idTipoPeriodoAcademico +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", ano=" + ano +
                ", periodo=" + periodo +
                ", fechaInicioClases=" + fechaInicioClases +
                ", fechaFinClases=" + fechaFinClases +
                ", codigoPeriodo=" + codigoPeriodo +
                ", actual=" + actual +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
/* 02/06/2026 @:Sebastian Jaimes */