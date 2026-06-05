/**
 * Aplicación: rvd
 * Archivo: ModalidadContratacionEntity.java
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
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MODALIDADCONTRATACION", schema = "CONTRATOS")
public class ModalidadContratacionEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOCO_ID", nullable = false)
    private Long id;

    @Column(name = "MOCO_NOMBRE")
    private String nombre;

    @Column(name = "MOCO_DESCRIPCION")
    private String descripcion;

    @Column(name = "MOCO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "MOCO_FECHACAMBIO")
    private Date fechaCambio;

    @Column(name = "MOCO_PORCENTAJEMAXIMOANTICIPO")
    private Long porcentajeMaximoAntic;
    
    @Column(name = "MOCO_INSTRUCTIVO")
    private String instructivo;

    @Column(name = "CLMO_ID")
    private Long idClasificacionModalidad;

    @Column(name = "MOCO_PORCENTAJEMINIMOANTICIPO")
    private Long porcentajeMinimoAntic;

    @Column(name = "MOCO_ESTADO")
    private String estado;

    @Column(name = "MOCO_SIGLA")
    private String sigla;

    

    /* TODO: 
    @OneToMany
    @JoinColumn(name = "CLMO_ID",  insertable = false, updatable = false)
    private List<ClaseModalidadEntity> convocatorias;*/

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ModalidadContratacionEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", porcentajeMaximoAntic=" + porcentajeMaximoAntic +
                ", instructivo=" + instructivo +
                ", idClasificacionModalidad=" + idClasificacionModalidad +
                ", porcentajeMinimoAntic=" + porcentajeMinimoAntic +
                ", estado=" + estado +
                ", sigla=" + sigla +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
