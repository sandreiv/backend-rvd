/**
 * Aplicación: rvd
 * Archivo: ObservacionesEntity.java
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
@Table(name = "OBSERVACIONES", schema = "RVD")
public class ObservacionesEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBSE_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID")
    private Long idCargaDocente;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "OBSE_TEXTO")
    private String texto;

    @Column(name = "OBSE_FECHA")
    private Date fecha;

    @Column(name = "OBSE_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "OBSE_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private List<CargaDocenteEntity> cargasDocente;

    @OneToMany
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private List<PersonaGeneralEntity> personasGenerales;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ObservacionesEntity{" +
                "id=" + id +
                ", idCargaDocente=" + idCargaDocente +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", texto=" + texto +
                ", fecha=" + fecha +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
