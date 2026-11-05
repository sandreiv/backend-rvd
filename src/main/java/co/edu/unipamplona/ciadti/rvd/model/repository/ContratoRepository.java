/**
 * Aplicación: rvd
 * Archivo: ContratoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;

public interface ContratoRepository extends JpaRepository<ContratoEntity, Long> {

    /**
     * Contrato registrado con un número de anexo, para no duplicar el registro
     * cuando el usuario vuelve a diligenciar el mismo contrato.
     */
    Optional<ContratoEntity> findFirstByNumeroOrderByIdDesc(String numero);
}
