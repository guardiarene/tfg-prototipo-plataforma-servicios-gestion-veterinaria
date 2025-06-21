package tfg.psygcv.repository.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.Treatment;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreatmentStatisticsRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t.product FROM Treatment t JOIN t.medicalRecord mr JOIN mr.veterinarian v JOIN v.clinicsOwned c WHERE c.id = :clinicId AND mr.date BETWEEN :startDate AND :endDate")
    List<String> getFrequentTreatmentsByClinicAndDate(@Param("clinicId") Long clinicId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
