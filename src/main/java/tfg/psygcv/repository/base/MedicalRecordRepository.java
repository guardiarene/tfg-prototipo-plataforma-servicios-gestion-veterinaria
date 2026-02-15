package tfg.psygcv.repository.base;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

  @Query(
      "SELECT mr FROM MedicalRecord mr WHERE mr.pet IN (SELECT a.pet FROM Appointment a WHERE a.clinic.id = :clinicId) AND mr.pet.active = true ORDER BY mr.createdAt ASC")
  List<MedicalRecord> findByClinicId(@Param("clinicId") Long clinicId);
}
