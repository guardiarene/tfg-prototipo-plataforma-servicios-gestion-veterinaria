package tfg.psygcv.repository.base;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

  @Query(
      "SELECT v FROM Vaccine v WHERE v.medicalRecord.id = :medicalRecordId AND v.active = true ORDER BY v.applicationDate DESC")
  List<Vaccine> findByMedicalRecordIdAndActiveOrderByApplicationDateDesc(
      @Param("medicalRecordId") Long medicalRecordId);

  @Query(
      "SELECT v FROM Vaccine v WHERE v.visit.id = :visitId AND v.active = true ORDER BY v.applicationDate DESC")
  List<Vaccine> findByVisitIdAndActiveOrderByApplicationDateDesc(@Param("visitId") Long visitId);
}
