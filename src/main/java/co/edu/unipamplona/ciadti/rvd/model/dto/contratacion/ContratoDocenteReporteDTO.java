/**
 * Aplicación: rvd
 * Archivo: ContratoDocenteReporteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (generación del anexo de contratación en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto.contratacion;

import java.util.List;

/**
 * Objeto raíz que consume la plantilla (${contrato}). Cada apartado del anexo
 * viaja en su propio objeto para que se pueda nutrir de una fuente distinta.
 */
public record ContratoDocenteReporteDTO(
    ContratoAnexoDTO anexo,
    ContratoDocenteDTO docente,
    ContratoAcademicoDTO academico,
    ContratoRemuneracionDTO remuneracion,
    ContratoDiscriminadoDTO discriminado,
    ContratoPlanTrabajoDTO planTrabajo,
    ContratoPresupuestoDTO presupuesto,
    ContratoSuscripcionDTO suscripcion,
    ContratoFirmasDTO firmas,
    List<String> funciones
) {}
