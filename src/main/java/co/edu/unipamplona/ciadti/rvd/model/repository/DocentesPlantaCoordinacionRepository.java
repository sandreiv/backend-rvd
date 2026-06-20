package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntityId;

public interface DocentesPlantaCoordinacionRepository extends
        JpaRepository<DocentesPlantaCoordinacionEntity,
                DocentesPlantaCoordinacionEntityId> {

    @Query("""
            SELECT dopc FROM DocentesPlantaCoordinacionEntity dopc
            INNER JOIN FETCH dopc.personaGeneral pege
            INNER JOIN FETCH pege.personaNaturalGeneral peng
            WHERE dopc.idCoordinacion = :idCoordinacion
            ORDER BY peng.primerApellido, peng.segundoApellido,
                peng.primerNombre, peng.segundoNombre
            """)
    List<DocentesPlantaCoordinacionEntity> findByIdCoordinacion(
            @Param("idCoordinacion") Long idCoordinacion);
}
