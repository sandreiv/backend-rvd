/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "CONVOCATORIAPROYECTOS", schema = "RVD")
public class ConvocatoriaProyectosEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COPR_ID", nullable = false)
    private Long id;

    @Column(name = "COPR_NOMBRE")
    private String nombre;

    @Column(name = "COPR_DESCRIPCION")
    private String descripcion;

    @Column(name = "COPR_CODIGO")
    private String codigo;

    @Column(name = "COPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COPR_FECHAFIN")
    private Date fechaFin;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ConvocatoriaProyectosEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", codigo=" + codigo +
                ", registradoPor=" + registradoPor +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
