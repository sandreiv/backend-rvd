/**
 * Aplicación: rvd
 * Archivo: PuntosCategoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/06/2026
 * Modificaciones:
 * 22/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PUNTOSCATEGORIA", schema = "RVD")
public class PuntosCategoriaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUCA_ID", nullable = false)
    private Long id;

    @Column(name = "CACA_ID", nullable = false)
    private Long idCategoriaCatedratico;

    @Column(name = "PUCA_PUNTOS")
    private String puntos;

    @Column(name = "PUCA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PUCA_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CACA_ID", insertable = false, updatable = false)
    private CategoriaCatedraticoEntity categoriaCatedratico;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PuntosCategoriaEntity{" +
                "id=" + id +
                ", idCategoriaCatedratico=" + idCategoriaCatedratico +
                ", puntos=" + puntos +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
/* 22/06/2026 @:Sebastian Jaimes */
