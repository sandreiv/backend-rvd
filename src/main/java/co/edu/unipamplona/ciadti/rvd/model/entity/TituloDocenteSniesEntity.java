package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "TITULODOCENTESNIES",
        schema = "ACADEMICO"
)
public class TituloDocenteSniesEntity
        implements Serializable {

    @Id
    @Column(name = "TITD_ID", nullable = false)
    private Long id;

    @Column(
            name = "TITD_DESCRIPCION",
            nullable = false
    )
    private String descripcion;

    @Column(
            name = "TITD_REGISTRADOPOR",
            nullable = false
    )
    private String registradoPor;

    @Column(
            name = "TITD_FECHACAMBIO",
            nullable = false
    )
    private Date fechaCambio;

    @Column(
            name = "TITD_NIVEL",
            nullable = false
    )
    private Long nivel;
}