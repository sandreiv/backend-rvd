/**
 * Aplicación: rvd
 * Archivo: CargaEntity.java
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
@Table(name = "CARGA", schema = "RVD")
public class CargaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARG_ID", nullable = false)
    private Long id;

    @Column(name = "CONV_ID")
    private Long idConvocatoria;

    @Column(name = "COOR_ID")
    private Long idCoordinacion;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "ESCA_ID")
    private Long idEstadoCarga;

    @Column(name = "CARG_VALOR")
    private String valor;

    @Column(name = "CARG_VALORAUTORIZADO")
    private String valorAutorizado;

    @Column(name = "CARG_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CARG_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "CONV_ID", insertable = false, updatable = false)
    private List<ConvocatoriaEntity> convocatorias;

    @OneToMany
    @JoinColumn(name = "COOR_ID", insertable = false, updatable = false)
    private List<CoordinacionesEntity> coordinaciones;

    @OneToMany
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private List<CentroCostoEntity> centrosCosto;

    @OneToMany
    @JoinColumn(name = "ESCA_ID", insertable = false, updatable = false)
    private List<EstadoCargaEntity> estadosCarga;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CargaEntity{" +
                "id=" + id +
                ", idConvocatoria=" + idConvocatoria +
                ", idCoordinacion=" + idCoordinacion +
                ", idCentroCosto=" + idCentroCosto +
                ", idEstadoCarga=" + idEstadoCarga +
                ", valor=" + valor +
                ", valorAutorizado=" + valorAutorizado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
