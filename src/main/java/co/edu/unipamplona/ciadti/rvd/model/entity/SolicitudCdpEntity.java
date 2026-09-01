/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/09/2026
 * Modificaciones:
 * 01/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "SOLICITUDCDP", schema = "RVD")
public class SolicitudCdpEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOCD_ID", nullable = false)
    private Long id;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "SOCD_ESTADO")
    private String estado;

    @Column(name = "SOCD_ADJUNTO")
    private String adjunto;

    @Column(name = "SOCD_OBSERVACION")
    private String observacion;

    @Column(name = "SOCD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "SOCD_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private CoordinacionesEntity coordinacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "SolicitudCdpEntity{"
                + "id=" + id
                + ", idCoordinacion=" + idCoordinacion
                + ", estado=" + estado
                + ", adjunto=" + adjunto
                + ", observacion=" + observacion
                + ", registradoPor=" + registradoPor
                + ", fechaCambio=" + fechaCambio
                + '}';
    }
}

