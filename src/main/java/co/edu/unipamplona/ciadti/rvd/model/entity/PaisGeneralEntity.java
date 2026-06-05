/**
 * Aplicación: rvd
 * Archivo: PaisGeneralEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "PAISGENERAL", schema = "GENERAL")
public class PaisGeneralEntity implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAGE_ID")
    private String id;

    @Column(name = "PAGE_NOMBRE")
    private String nombre;

    @Column(name = "PAGE_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PAGE_FECHACAMBIO")
    private Date fechaCambio;
}

/* 02/06/2026 @:Sebastian Jaimes */
