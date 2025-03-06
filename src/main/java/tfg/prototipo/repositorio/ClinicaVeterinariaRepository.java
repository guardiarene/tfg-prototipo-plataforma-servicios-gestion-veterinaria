package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.ClinicaVeterinaria;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicaVeterinariaRepository extends JpaRepository<ClinicaVeterinaria, Long> {

    @Query("SELECT DISTINCT c FROM ClinicaVeterinaria c LEFT JOIN FETCH c.servicios s WHERE c.activo = true AND (s.activo = true OR s IS NULL)")
    List<ClinicaVeterinaria> obtenerTodas();

    @Query("SELECT c FROM ClinicaVeterinaria c LEFT JOIN FETCH c.servicios s WHERE c.id = :idClinica AND (s.activo IS NULL OR s.activo = true)")
    Optional<ClinicaVeterinaria> obtenerPorId(@Param("idClinica") Long idClinica);

    List<ClinicaVeterinaria> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM ClinicaVeterinaria c WHERE c.veterinario.id = :idVeterinario")
    ClinicaVeterinaria obtenerPorIdVeterinario(@Param("idVeterinario") Long idVeterinario);

    Optional<ClinicaVeterinaria> findByRecepcionistaId(Long recepcionistaId);

}
