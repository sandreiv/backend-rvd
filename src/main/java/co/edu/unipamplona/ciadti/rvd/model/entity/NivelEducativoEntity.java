/**
 * Aplicación: rvd
 * Archivo: NivelEducativoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "NIVELEDUCATIVO", schema = "ACADEMICO")
public class NivelEducativoEntity implements Serializable, Cloneable {


    @Id
    @Column(name = "NIED_ID", nullable = false)
    private Long id;

    @Column(name = "NIED_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "NIED_FECHACAMBIO")
    private Date fechaCambio;

    @Column(name = "NIED_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "NIED_PARA_IES")
    private String paraIes;

    @Column(name = "NIED_OBSERVACION")
    private String observacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "NivelEducativoEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", fechaCambio=" + fechaCambio +
                ", registradoPor=" + registradoPor +
                ", paraIes=" + paraIes +
                ", observacion=" + observacion +
                '}';
    }

}

/* 02/06/2026 @:Sebastian Jaimes */

