/**
 * Aplicación: rvd
 * Archivo: PuntosVigenciaEntity.java
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
@Table(name = "PUNTOSVIGENCIA", schema = "RVD")
public class PuntosVigenciaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUVI_ID", nullable = false)
    private Long id;

    @Column(name = "PUVI_ANIO", nullable = false)
    private Long anio;

    @Column(name = "PUVI_VALORPUNTO")
    private String valorPunto;

    @Column(name = "PUVI_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PUVI_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PuntosVigenciaEntity{" +
                "id=" + id +
                ", anio=" + anio +
                ", valorPunto=" + valorPunto +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
/* 22/06/2026 @:Sebastian Jaimes */
