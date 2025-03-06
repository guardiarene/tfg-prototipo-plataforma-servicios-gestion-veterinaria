package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.ServicioMedico;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioMedicoRepository extends JpaRepository<ServicioMedico, Long> {

    @Query("SELECT s FROM ServicioMedico s WHERE s.id = :servicioId AND s.activo = true")
    Optional<ServicioMedico> obtenerPorId(@Param("servicioId") Long servicioId);

    @Query("SELECT s FROM ServicioMedico s WHERE s.clinica.id = :clinicaId AND s.activo = true")
    List<ServicioMedico> obtenerPorIdClinica(@Param("clinicaId") Long clinicaId);

}
