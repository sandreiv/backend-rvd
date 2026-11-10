/**
 * Aplicación: rvd
 * Archivo: DedicacionDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

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
@Table(name = "DEDICACIONDOCENTE", schema = "ACADEMICO")
public class DedicacionDocenteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEDO_ID", nullable = false)
    private Long id;

    @Column(name = "DEDO_DESCRIPCION")
    private String descripcion;

    @Column(name = "DEDO_CARGAMINIMA")
    private Long cargaMinima;

    @Column(name = "DEDO_CARGAMAXIMA")
    private Long cargaMaxima;

    @Column(name = "DEDO_PERMITIR")
    private String permitir;

    @Column(name = "DEDO_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "DEDO_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DedicacionDocenteEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", cargaMinima=" + cargaMinima +
                ", cargaMaxima=" + cargaMaxima +
                ", permitir=" + permitir +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
