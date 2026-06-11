/**
 * Aplicación: rvd
 * Archivo: DocentesPlantaCoordinacEntity.java
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
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(DocentesPlantaCoordinacionEntityId.class)
@Table(name = "DOCENTESPLANTACOORDINACION", schema = "RVD")
public class DocentesPlantaCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Id
    @Column(name = "COOR_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "DOPC_ESTADO")
    private Long estado;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DocentesPlantaCoordinacEntity{" +
                "idPersonaGeneral=" + idPersonaGeneral +
                ", idCoordinacion=" + idCoordinacion +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
