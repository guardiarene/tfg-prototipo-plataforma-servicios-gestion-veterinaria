package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.HistoriaClinica;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    @Query("SELECT hc FROM HistoriaClinica hc WHERE hc.paciente IN (SELECT t.mascota FROM Turno t WHERE t.clinica.id = :clinicaId) AND hc.paciente.activo = true ORDER BY hc.fecha ASC")
    List<HistoriaClinica> obtenerPorIdClinica(@Param("clinicaId") Long clinicaId);

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.examenClinico LEFT JOIN FETCH h.anamnesis WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerHistoriaBase(@Param("id") Long id);

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.tratamientos WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerConTratamientos(@Param("id") Long id);

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.diagnosticos WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerConDiagnosticos(@Param("id") Long id);

    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.anamnesis a LEFT JOIN FETCH a.vacunas WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerConVacunas(@Param("id") Long id);

    @Query("SELECT DISTINCT h FROM HistoriaClinica h LEFT JOIN FETCH h.examenClinico LEFT JOIN FETCH h.anamnesis a LEFT JOIN FETCH a.vacunas WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerHistoriaClinicaConVacunas(@Param("id") Long id);

    @Query("SELECT DISTINCT h FROM HistoriaClinica h LEFT JOIN FETCH h.tratamientos WHERE h.id = :id")
    Optional<HistoriaClinica> obtenerHistoriaClinicaConTratamientos(@Param("id") Long id);

}
