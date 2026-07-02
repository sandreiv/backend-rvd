/**
 * Aplicación: rvd
 * Archivo: RelacionCargaProyectoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "RELACIONCARGAPROYECTO", schema = "RVD")
public class RelacionCargaProyectoEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECP_ID")
    private Long id;

    @Column(name = "PEPR_ID")
    private Long idPersonaProyecto;

    @Column(name = "DECD_ID")
    private Long idDetalleCargaDocente;

    @Column(name = "DECD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "DECD_FECHACAMBIO")
    private Date fechaCambio;
}
/* 02/06/2026 @:Sebastian Jaimes */
