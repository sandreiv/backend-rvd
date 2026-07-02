/**
 * Aplicación: rvd
 * Archivo: UnidadProgramaEntity.java
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
@Table(name = "UNIDADPROGRAMA", schema = "ACADEMICO")
public class UnidadProgramaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNPR_ID", nullable = false)
    private Long id;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "UNPR_RELACION")
    private String relacion;

    @Column(name = "UNPR_CUPOMINIMO")
    private Long cupoMinimo;

    @Column(name = "UNPR_CUPOMAXIMO")
    private Long cupoMaximo;

    @Column(name = "UNPR_NUMEROOPCIONADOS")
    private Long numeroOpcionados;

    @Column(name = "UNPR_ESFACULTAD")
    private String esFacultad;

    @Column(name = "UNPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "UNPR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "UnidadProgramaEntity{" +
                "id=" + id +
                ", idUnidad=" + idUnidad +
                ", idPrograma=" + idPrograma +
                ", relacion=" + relacion +
                ", cupoMinimo=" + cupoMinimo +
                ", cupoMaximo=" + cupoMaximo +
                ", numeroOpcionados=" + numeroOpcionados +
                ", esFacultad=" + esFacultad +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
/* 30/06/2026 @:Sebastian Jaimes */
