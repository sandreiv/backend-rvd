/**
 * Aplicación: rvd
 * Archivo: PensumEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PENSUM", schema = "ACADEMICO")
public class PensumEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PENS_ID", nullable = false)
    private Long id;

    @Column(name = "PROG_ID", nullable = false)
    private Long idPrograma;

    @Column(name = "ESPE_ID", nullable = false)
    private Long idEspecialidad;

    @Column(name = "PENS_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "PENS_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PENS_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "TIPA_ID")
    private Long idTipa;

    @Column(name = "PENS_ANOINICIO")
    private Long anioInicio;

    @Column(name = "PENS_PERIODOINICIO")
    private String periodoInicio;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "TPPA_ID")
    private Long idTipoPeriodoAcademico;

    @Column(name = "PENS_NUMPERIODOS")
    private Long numPeriodos;

    @Column(name = "PENS_TOTALCREDITOS")
    private String totalCreditos;

    @Column(name = "PENS_REDONDEOPROMEDIO3")
    private Long redondeoPromedio3;

    @Column(name = "PENS_TIPOPROMEDIO")
    private String tipoPromedio;

    @Column(name = "PENS_REDONDEOPROMEDIO")
    private Long redondeoPromedio;

    @Column(name = "PENS_REDONDEODEFINITIVA3")
    private Long redondeoDefinitiva3;

    @Column(name = "PENS_PONMINMATNOR")
    private String ponMinMatNor;

    @Column(name = "PENS_PONMINMATELE")
    private String ponMinMatEle;

    @Column(name = "PENS_FECHARECIBEESTUDIANTES")
    private Date fechaRecibeEstudiantes;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PensumEntity{" +
                "id=" + id +
                ", idPrograma=" + idPrograma +
                ", idEspecialidad=" + idEspecialidad +
                ", descripcion=" + descripcion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idTipa=" + idTipa +
                ", anioInicio=" + anioInicio +
                ", periodoInicio=" + periodoInicio +
                ", idNormaGeneral=" + idNormaGeneral +
                ", idTipoPeriodoAcademico=" + idTipoPeriodoAcademico +
                ", numPeriodos=" + numPeriodos +
                ", totalCreditos=" + totalCreditos +
                ", redondeoPromedio3=" + redondeoPromedio3 +
                ", tipoPromedio=" + tipoPromedio +
                ", redondeoPromedio=" + redondeoPromedio +
                ", redondeoDefinitiva3=" + redondeoDefinitiva3 +
                ", ponMinMatNor=" + ponMinMatNor +
                ", ponMinMatEle=" + ponMinMatEle +
                ", fechaRecibeEstudiantes=" + fechaRecibeEstudiantes +
                '}';
    }
}
/* 30/06/2026 @:Sebastian Jaimes */
