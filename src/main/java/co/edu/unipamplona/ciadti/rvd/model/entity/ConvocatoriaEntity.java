/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaEntity.java
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
@Table(name = "CONVOCATORIA", schema = "RVD")
public class ConvocatoriaEntity implements Serializable, Cloneable{
    @Id
    @Column(name = "CONV_ID", nullable = false)
    private Long id;

    @Column(name = "PEUN_ID", nullable = false)
    private Long idPeriodoUniversidad;

    @Column(name = "NIED_ID", nullable = false)
    private Long idNivelEducativo;

    @Column(name = "PEGE_IDAUTORIZA", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "CONV_NOMBRE")
    private String nombre;

    @Column(name = "CONV_DESCRIPCION")
    private String descripcion;

    @Column(name = "CONV_ESTADO")
    private String estado;

    @Column(name = "CONV_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CONV_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "PEUN_ID",  insertable = false, updatable = false)
    private List<PeriodoUniversidadEntity> periodosUniversidad;

    @OneToMany
    @JoinColumn(name = "NIED_ID",  insertable = false, updatable = false)
    private List<NivelEducativoEntity> nivelesEducativos;

    @OneToMany
    @JoinColumn(name = "PEGE_IDAUTORIZA",  insertable = false, updatable = false)
    private List<PersonaGeneralEntity> personasGenerales;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ConvocatoriaEntity{" +
                "id=" + id +
                ", idPeriodoUniversidad=" + idPeriodoUniversidad +
                ", idNivelEducativo=" + idNivelEducativo +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", estado=" + estado +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */