package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.Diagnostico;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    @Query("SELECT d.problemas FROM Diagnostico d JOIN d.historiaClinica h JOIN h.veterinario v JOIN v.clinicas c WHERE c.id = :clinicaId AND h.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<String> obtenerProblemasFrecuentesPorClinicaYFecha(@Param("clinicaId") Long clinicaId, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

}
