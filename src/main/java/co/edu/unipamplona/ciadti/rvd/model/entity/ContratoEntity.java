/**
 * Aplicación: rvd
 * Archivo: ContratoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial (registro del contrato del docente desde el anexo)
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Contrato del docente (CONTRATOS.CONTRATO).
 *
 * <p>El PK lo llena el trigger {@code TRG_CONTRATO} desde {@code SEQ_CONTRATO}.
 * Las columnas que RVD todavía no sabe diligenciar quedan nulas.</p>
 */
@Getter
@Setter
@Entity
@Table(name = "CONTRATO", schema = "CONTRATOS")
public class ContratoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONT_ID", nullable = false)
    private Long id;

    /** Modalidad de contratación (CONTRATOS.MODALIDADCONTRATACION). */
    @Column(name = "MOCO_ID", nullable = false)
    private Long idModalidadContratacion;

    @Column(name = "CONV_ID")
    private Long idConvocatoria;

    /** Ciudad de la unidad regional (GENERAL.CIUDADGENERAL). */
    @Column(name = "CIGE_ID")
    private Long idCiudad;

    /** Número de anexo diligenciado por el usuario. */
    @Column(name = "CONT_NUMERO")
    private String numero;

    /** Fecha de ingreso diligenciada por el usuario. */
    @Column(name = "CONT_FECHAFIRMA")
    private Date fechaFirma;

    @Column(name = "CONT_FECHAINICIO", nullable = false)
    private Date fechaInicio;

    @Column(name = "CONT_FECHAFIN")
    private Date fechaFin;

    /** Valor total del contrato. */
    @Column(name = "CONT_CUANTIA", nullable = false)
    private BigDecimal cuantia;

    @Column(name = "CONT_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "CONT_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    /** Forma de pago (BIENESV3.FORMAPAGO). */
    @Column(name = "FOPA_ID")
    private Long idFormaPago;

    /** Persona que contrata (usuario autenticado). */
    @Column(name = "PEGE_IDCONTRATANTE", nullable = false)
    private Long idPersonaContratante;

    /** Docente contratado. */
    @Column(name = "PEGE_IDCONTRATISTA", nullable = false)
    private Long idPersonaContratista;

    @Column(name = "CONT_ANTICIPO")
    private BigDecimal anticipo;

    @Column(name = "COVE_ID")
    private Long idCove;

    @Column(name = "SOCO_ID")
    private Long idSolicitudContrato;

    @Column(name = "CONT_ANULADO", nullable = false)
    private String anulado;

    @Column(name = "PSPS_ID")
    private Long idPsps;

    @Column(name = "CALI_ID")
    private Long idCalificacion;

    @Column(name = "CONT_JUSTIFICACIONANTICIPO")
    private String justificacionAnticipo;

    @Column(name = "CONT_INICIADO", nullable = false)
    private String iniciado;

    /** Forma de inicio del contrato (CONTRATOS.FORMAINICIOCONTRATO). */
    @Column(name = "FOIC_ID", nullable = false)
    private Long idFormaInicioContrato;

    @Column(name = "DOSO_ID")
    private Long idDoso;

    /** Tipo de objeto contractual (CONTRATOS.TIPOOBJETO). */
    @Column(name = "TIOB_ID")
    private Long idTipoObjeto;

    @Column(name = "NORG_ID")
    private Long idNorg;

    @Column(name = "PEGE_IDUNIVERSIDADCOMISION")
    private Long idPersonaUniversidadComision;

    @Column(name = "CONT_ESTUDIOSUPERIOR")
    private String estudioSuperior;

    @Column(name = "SOCO_IDCOTIZACION")
    private Long idSolicitudCotizacion;

    @Column(name = "REQU_ID")
    private Long idRequerimiento;

    /** Duración en meses, calculada de las fechas del contrato. */
    @Column(name = "CONT_DURACIONCONTRATO")
    private String duracionContrato;

    @Column(name = "CONT_ORIGEN")
    private String origen;

    @Column(name = "CONT_ESABIERTO")
    private String esAbierto;

    @Column(name = "CDP_ID")
    private Long idCdp;

    @Column(name = "CONT_REGISTROAUTOMATICO", nullable = false)
    private String registroAutomatico;

    @Column(name = "CONT_NUMEROHORAS")
    private BigDecimal numeroHoras;

    @Column(name = "CONT_VALORSALARIO")
    private String valorSalario;

    @Column(name = "TPRE_ID")
    private Long idTpre;

    @Column(name = "CAAS_ID")
    private Long idCaas;

    /** Unidad regional del docente (ACADEMICO.UNIDAD). */
    @Column(name = "UNID_ID", nullable = false)
    private Long idUnidad;

    @Column(name = "CONT_FECHAFINPORACTA")
    private Date fechaFinPorActa;

    @Column(name = "CONT_TIPODIAS", nullable = false)
    private String tipoDias;

    @Column(name = "CONT_SIGLA")
    private String sigla;

    @Column(name = "UPRO_ID")
    private Long idUpro;

    @Column(name = "CONT_DISTRIBUCIONCARGA")
    private String distribucionCarga;

    @Lob
    @Column(name = "CONT_OBJETO")
    private String objeto;

    @Lob
    @Column(name = "CONT_DESCRIPCION")
    private String descripcion;

    @Column(name = "CONT_DIRECCIONSECOP")
    private String direccionSecop;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ContratoEntity{" +
                "id=" + id +
                ", idModalidadContratacion=" + idModalidadContratacion +
                ", numero=" + numero +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", cuantia=" + cuantia +
                ", idFormaPago=" + idFormaPago +
                ", idTipoObjeto=" + idTipoObjeto +
                ", idPersonaContratante=" + idPersonaContratante +
                ", idPersonaContratista=" + idPersonaContratista +
                ", idUnidad=" + idUnidad +
                '}';
    }
}
