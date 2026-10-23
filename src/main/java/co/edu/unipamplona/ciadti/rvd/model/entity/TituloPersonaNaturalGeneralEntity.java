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
        name = "TITULOPERSONANATURALGENERAL",
        schema = "GENERAL"
)
public class TituloPersonaNaturalGeneralEntity
        implements Serializable {

    @Id
    @Column(name = "TPNG_ID")
    private Long id;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "TITU_ID", nullable = false)
    private Long idTitulo;

    @Column(
            name = "TIPN_ESTABLECIMIENTO",
            length = 160
    )
    private String establecimiento;

    @Column(
            name = "TIPN_REGISTRADOPOR",
            length = 500
    )
    private String registradoPor;

    @Column(
            name = "TIPN_FECHACAMBIO",
            nullable = false
    )
    private Date fechaCambio;

    @Column(
            name = "PAGE_ID",
            nullable = false,
            length = 2
    )
    private String idPais;

    @Column(name = "TIPN_FECHAOBTENCION")
    private Date fechaObtencion;
}