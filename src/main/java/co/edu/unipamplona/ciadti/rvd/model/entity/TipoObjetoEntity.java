/**
 * Aplicación: rvd
 * Archivo: TipoObjetoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
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

/**
 * Tipo de objeto contractual (CONTRATOS.TIPOOBJETO).
 * Catálogo compartido con el módulo de contratos: aquí solo se lee.
 */
@Getter
@Setter
@Entity
@Table(name = "TIPOOBJETO", schema = "CONTRATOS")
public class TipoObjetoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIOB_ID", nullable = false)
    private Long id;

    @Column(name = "TIOB_DESCRIPCION")
    private String descripcion;

    @Column(name = "TIOB_ESTADO")
    private String estado;

    @Column(name = "TIOB_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIOB_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoObjetoEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
