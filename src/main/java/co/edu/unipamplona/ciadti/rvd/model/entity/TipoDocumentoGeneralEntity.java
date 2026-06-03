/**
 * Aplicación: rvd
 * Archivo: TipoDocumentoGeneralEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TIPODOCUMENTOGENERAL", schema = "GENERAL")
public class TipoDocumentoGeneralEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIDG_ID")
    private Long id;

    @Column(name = "TIDG_DESCRIPCION")
    private String descripcion;

    @Column(name = "TIDG_TIPOPERSONA")
    private String tipoPersona;

    @Column(name = "TIDG_ABREVIATURA")
    private String abreviatura;

    @Column(name = "TIDG_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIDG_FECHACAMBIO")
    private Date fechaCambio;
}


/* 02/06/2026 @:Sebastian Jaimes */
