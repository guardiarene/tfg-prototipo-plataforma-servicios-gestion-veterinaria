package tfg.psygcv.repository.statistics;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.Diagnostic;

@Repository
public interface DiagnosticStatisticsRepository extends JpaRepository<Diagnostic, Long> {

  @Query(
      "SELECT d.problems FROM Diagnostic d JOIN d.visit v JOIN v.medicalRecord mr JOIN v.veterinarian vet JOIN vet.clinicsOwned c WHERE c.id = :clinicId AND v.date BETWEEN :startDate AND :endDate")
  List<String> getFrequentProblemsByClinicAndDate(
      @Param("clinicId") Long clinicId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
