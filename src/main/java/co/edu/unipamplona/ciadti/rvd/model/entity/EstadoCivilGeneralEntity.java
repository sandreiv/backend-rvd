/**
 * Aplicación: rvd
 * Archivo: EstadoCivilGeneralEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ESTADOCIVILGENERAL", schema = "GENERAL")
public class EstadoCivilGeneralEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESCG_ID")
    private Long id;

    @Column(name = "ESCG_DESCRIPCION")
    private String descripcion;

    @Column(name = "ESCG_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "ESCG_FECHACAMBIO")
    private Date fechaCambio;
}

/* 02/06/2026 @:Sebastian Jaimes */
