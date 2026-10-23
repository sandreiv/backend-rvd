package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface PublicAcademicTrainingProjection {

    Long getId();

    Long getIdNivelAcademico();

    String getNivelAcademico();

    Long getIdTitulo();

    String getTitulo();

    String getInstitucion();

    Date getFechaGrado();

    String getIdPais();

    String getPais();
}