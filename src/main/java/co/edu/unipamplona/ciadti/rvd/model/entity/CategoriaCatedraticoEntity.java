/**
 * Aplicación: rvd
 * Archivo: CategoriaCatedraticoEntity.java
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
@Table(name = "CATEGORIACATEDRATICO", schema = "TALENTOV3")
public class CategoriaCatedraticoEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "CACA_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CACA_DESCRIPCION")
    private String descripcion;

    @Column(name = "CACA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CACA_FECHACAMBIO")
    private Date fechaCambio;

    @Column(name = "CACA_JERARQUIA")
    private Long jerarquia;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CategoriaCatedraticoEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", jerarquia=" + jerarquia +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
