/**
 * Aplicación: rvd
 * Archivo: PersonaGeneralFoto.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PERSONAGENERALFOTO", schema = "GENERAL")
public class PersonaGeneralFotoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PGFO_IDENTIFICADOR", nullable = false)
    private Long id;

    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PGFO_ARCHIVO", nullable = false)
    private byte[] archivo;

    @Column(name = "PGFO_EXTENSION", nullable = false)
    private String extension;

    @Column(name = "PGFO_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PGFO_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneral;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

/* 7/04/2026 @:Leonel Antonio Pérez Ríos */
