package tfg.psygcv.repository.medical;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.medical.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

  @Query(
      "SELECT DISTINCT mr FROM MedicalRecord mr JOIN FETCH mr.pet p JOIN FETCH p.owner WHERE mr.pet IN (SELECT a.pet FROM Appointment a WHERE a.clinic.id = :clinicId) AND mr.pet.active = true ORDER BY mr.createdAt ASC")
  List<MedicalRecord> findByClinicId(@Param("clinicId") Long clinicId);
}
