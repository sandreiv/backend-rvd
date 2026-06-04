package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;

public interface ConvocatoriaRepository extends JpaRepository<ConvocatoriaEntity, Long> {

    @Query(value = """
            SELECT
                CONV.CONV_DESCRIPCION AS descripcion,
                FECO.FECHA_INICIO AS fechaInicio,
                FECO.FECO_FECHAFIN AS fechaFin,
                PENG.PENG_PRIMERNOMBRE
                || ' ' || PENG.PENG_SEGUNDONOMBRE
                || ' ' || PENG.PENG_PRIMERAPELLIDO
                || ' ' || PENG.PENG_SEGUNDOAPELLIDO
                AS nombreCompleto
            FROM RVD.FECHASCONVOCATORIA FECO
            LEFT JOIN RVD.CONVOCATORIA CONV
                ON FECO.CONV_ID = CONV.CONV_ID
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON CONV.PEGE_IDAUTORIZA = PEGE.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PEGE.PEGE_ID = PENG.PEGE_ID
            """, nativeQuery = true)
    List<ConvocatoriaDTO> findCallListWithDates();
    
}
