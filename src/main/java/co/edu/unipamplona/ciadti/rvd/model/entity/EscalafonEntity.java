/**
 * Aplicación: rvd
 * Archivo: EscalafonEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 24/06/2026
 * Modificaciones:
 * 24/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "ESCALAFON", schema = "COMITES")
public class EscalafonEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESCA_ID", nullable = false)
    private Long id;

    @Column(name = "CACA_ID")
    private Long idCategoriaCatedratico;

    @Column(name = "MOCO_ID")
    private Long idModalidadContratacion;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "ESCA_PUNTOS")
    private String puntos;

    @Column(name = "ESCA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "ESCA_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "CACA_ID", insertable = false, updatable = false)
    private List<CategoriaCatedraticoEntity> categoriaCatedratico;

    @OneToMany
    @JoinColumn(name = "MOCO_ID", insertable = false, updatable = false)
    private List<ModalidadContratacionEntity> modalidadContratacion;

    @OneToMany
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private List<PersonaGeneralEntity> personaGeneral;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "EscalafonEntity{" +
                "id=" + id +
                ", idCategoriaCatedratico=" + idCategoriaCatedratico +
                ", idModalidadContratacion=" + idModalidadContratacion +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", puntos=" + puntos +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
