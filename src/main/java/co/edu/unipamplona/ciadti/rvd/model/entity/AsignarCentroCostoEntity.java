/**
 * Aplicación: rvd
 * Archivo: AsignarCentroCostoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "ASIGNARCENTROCOSTO", schema = "RVD")
public class AsignarCentroCostoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASCC_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "ASCC_ESTADO")
    private Long estado;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private List<CoordinacionesEntity> coordinaciones;

    @OneToMany
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private List<CentroCostoEntity> centrosCosto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "AsignarCentroCostoEntity{" +
                "id=" + id +
                ", idCoordinacion=" + idCoordinacion +
                ", idCentroCosto=" + idCentroCosto +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
