/**
 * Aplicación: rvd
 * Archivo: AsociacionCoordinacionEntity.java
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
@Table(name = "ASOCIACIONCOORDINACION", schema = "RVD")
public class AsociacionCoordinacionEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASCO_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "MATE_CODIGOMATERIA")
    private String codigoMateria;

    @Column(name = "ASCO_ESTADO")
    private String estado;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private List<CoordinacionesEntity> coordinaciones;

    @OneToMany
    @JoinColumn(name = "PROG_ID", insertable = false, updatable = false)
    private List<ProgramaEntity> programas;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "AsociacionCoordinacionEntity{" +
                "id=" + id +
                ", idCoordinacion=" + idCoordinacion +
                ", idPrograma=" + idPrograma +
                ", codigoMateria=" + codigoMateria +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
