/**
 * Aplicación: rvd
 * Archivo: TipoPeriodoAcademicoEntity.java
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
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TIPOPERIODOACADEMICO", schema = "ACADEMICO")
public class TipoPeriodoAcademicoEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "TPPA_ID", nullable = false)
    private Long id;

    @Column(name = "TPPA_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "TPPA_DURACIONSEMANAS", nullable = false)
    private String duracionSemanas;
    
    @Column(name = "TPPA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TPPA_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return "TipoPeriodoAcademicoEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", duracionSemanas=" + duracionSemanas +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
/* 02/06/2026 @:Sebastian Jaimes */