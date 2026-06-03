/**
 * Aplicación: rvd
 * Archivo: TipoActividadModalidadEntity.java
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "TIPOACTIVIDADMODALIDAD", schema = "RVD")
public class TipoActividadModalidadEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "TIAM_ID", nullable = false)
    private Long id;

    @Column(name = "MOCO_ID", nullable = false)
    private Long idModalidadContratacion;

    @Column(name = "TIAC_ID", nullable = false)
    private Long idTipoActividades;

    @Column(name = "TIAM_ORDEN")
    private String orden;

    @Column(name = "TIAM_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIAM_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "MOCO_ID", insertable = false, updatable = false)
    private List<ModalidadContratacionEntity> modalidadesContratacion;

    @OneToMany
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private List<TipoActividadesEntity> tipoActividades;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoActividadModalidadEntity{" +
                "id=" + id +
                ", idModalidadContratacion=" + idModalidadContratacion +
                ", idTipoActividades=" + idTipoActividades +
                ", orden=" + orden +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
