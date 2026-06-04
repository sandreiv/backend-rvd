/**
 * Aplicación: rvd
 * Archivo: RestriccionPorCoordinacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "RESTRICCIONPORCOORDINACION", schema = "RVD")
public class RestriccionPorCoordinacionEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "REXC_ID", nullable = false)
    private Long id;

    @Column(name = "FECO_ID", nullable = false)
    private Long idFechasConvocatoria;

    @Column(name = "COORD_ID", nullable = false)
    private Long idCoordinacion;

    @Column(name = "REXC_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "REXC_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "FECO_ID", insertable = false, updatable = false)
    private List<FechasConvocatoriaEntity> fechasConvocatoria;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RestriccionPorCoordinacionEntity{" +
                "id=" + id +
                ", idFechasConvocatoria=" + idFechasConvocatoria +
                ", idCoordinacion=" + idCoordinacion +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
