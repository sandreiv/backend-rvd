/**
 * Aplicación: rvd
 * Archivo: FormaInicioContratoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Andrés Hernández - Creación inicial
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
@Table(name = "FORMAINICIOCONTRATO", schema = "CONTRATOS")
public class FormaInicioContratoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOIC_ID", nullable = false)
    private Long id;

    @Column(name = "FOIC_NOMBRE")
    private String nombre;

    @Column(name = "FOIC_CLASE")
    private String clase;

    @Column(name = "FOIC_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "FOIC_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FormaInicioContratoEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", clase=" + clase +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
