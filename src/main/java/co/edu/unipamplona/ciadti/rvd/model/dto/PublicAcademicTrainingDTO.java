package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record PublicAcademicTrainingDTO(
        Long id,
        Long idNivelAcademico,
        String nivelAcademico,
        Long idTitulo,
        String titulo,
        String institucion,
        LocalDate fechaGrado,
        String idPais,
        String pais
) {
}