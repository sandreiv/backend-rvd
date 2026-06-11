/**
 * Aplicación: rvd
 * Archivo: RelacionConvocatoriasEntity.java
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
@IdClass(RelacionConvocatoriasEntityId.class)
@Table(name = "RELACIONCONVOCATORIAS", schema = "RVD")
public class RelacionConvocatoriasEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "COPR_ID", nullable = false)
    private Long idConvocatoriaProyectos;

    @Id
    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "RECO_ESTADO")
    private String estado;

    @Column(name = "RECO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "RECO_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RelacionConvocatoriasEntity{" +
                "idConvocatoriaProyectos=" + idConvocatoriaProyectos +
                ", idConvocatoria=" + idConvocatoria +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
