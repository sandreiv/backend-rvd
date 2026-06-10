package co.edu.unipamplona.ciadti.rvd.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "METODOLOGIA", schema = "ACADEMICO")
public class MetodologiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "METO_ID", nullable = false)
    private Long id;

    @Column(name = "METO_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "METO_ACTIVO")
    private String activo;

    @Column(name = "METO_FECHACAMBIO", nullable = false)
    private String fechaCambio;

    @Column(name = "METO_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return "MetodologiaEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", activo=" + activo +
                ", fechaCambio=" + fechaCambio +
                ", registradoPor=" + registradoPor +
                '}';
    }
}
