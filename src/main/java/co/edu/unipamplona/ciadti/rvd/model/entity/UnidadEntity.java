/**
 * Aplicación: rvd
 * Archivo: UnidadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;

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
@Table(name = "UNIDAD", schema = "ACADEMICO")
public class UnidadEntity implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNID_ID", nullable = false)
    private Long id;

    @Column(name = "UNID_ACTIVA")
    private String activa;

    @Column(name = "UNID_CODIGO")
    private String codigo;

    @Column(name = "UNID_EMAIL")
    private String email;

    @Column(name = "UNID_NOMBRE")
    private String nombre;

    @Column(name = "UNID_REGIONAL")
    private String regional;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "UnidadEntity{" +
                "id=" + id +
                ", activa=" + activa +
                ", codigo=" + codigo +
                ", email=" + email +
                ", nombre=" + nombre +
                ", regional=" + regional +
                '}';
    }
}

