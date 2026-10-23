package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PublicAcademicTrainingSaveDTO(

        @NotNull
        @Min(0)
        Long idNivelAcademico,

        @NotNull
        @Positive
        Long idTitulo,

        @NotBlank
        @Size(max = 160)
        String institucion,

        @NotNull
        LocalDate fechaGrado,

        @NotBlank
        @Size(max = 2)
        String idPais

) {
}