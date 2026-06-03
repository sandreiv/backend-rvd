/**
 * Aplicación: rvd
 * Archivo: FechasConvocatoriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
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
@Table(name = "FECHASCONVOCATORIA", schema = "RVD")
public class FechasConvocatoriaEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "FECO_ID", nullable = false)
    private Long id;

    @Column(name = "COTC_ID", nullable = false)
    private Long idConvocatoriaTipoContratacion;

    @Column(name = "CONV_ID", nullable = false)
    private Long idConvocatoria;

    @Column(name = "TIAC_ID", nullable = false)
    private Long idTipoActividades;

    @OneToMany
    @JoinColumn(name = "COTC_ID",  insertable = false, updatable = false)
    private List<ConvocatoriaTipoContratacionEntity> convocatoriaTipoContratacion;

    @OneToMany
    @JoinColumn(name = "TIAC_ID",  insertable = false, updatable = false)
    private List<TipoActividadesEntity> tipoActividades;

    @OneToMany
    @JoinColumn(name = "CONV_ID",  insertable = false, updatable = false)
    private List<ConvocatoriaEntity> convocatorias;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FechasConvocatoriaEntity{" +
                "id=" + id +
                ", idConvocatoriaTipoContratacion=" + idConvocatoriaTipoContratacion +
                ", idConvocatoria=" + idConvocatoria +
                ", idTipoActividades=" + idTipoActividades +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
