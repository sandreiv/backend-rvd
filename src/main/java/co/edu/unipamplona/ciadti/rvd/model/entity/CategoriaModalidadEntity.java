/**
 * Aplicación: rvd
 * Archivo: CategoriaModalidadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/06/2026
 * Modificaciones:
 * 26/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "CATEGORIAMODALIDAD", schema = "RVD")
public class CategoriaModalidadEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAMO_ID", nullable = false)
    private Long id;

    @Column(name = "CACA_ID")
    private Long idCategoriaCatedratico;

    @Column(name = "MOCO_ID")
    private Long idModalidadContratacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CategoriaModalidadEntity{" +
                "id=" + id +
                ", idCategoriaCatedratico=" + idCategoriaCatedratico +
                ", idModalidadContratacion=" + idModalidadContratacion +
                '}';
    }
}
/* 26/06/2026 @:Sebastian Jaimes */
