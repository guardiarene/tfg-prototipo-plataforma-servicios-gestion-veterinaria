package tfg.psygcv.medical.record.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.record.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

  @Query(
      "SELECT DISTINCT mr FROM MedicalRecord mr JOIN FETCH mr.pet p JOIN FETCH p.owner WHERE mr.pet IN (SELECT a.pet FROM Appointment a WHERE a.clinic.id = :clinicId) AND mr.pet.active = true ORDER BY mr.createdAt ASC")
  List<MedicalRecord> findByClinicId(@Param("clinicId") Long clinicId);

  @Query(
      "SELECT CASE WHEN COUNT(mr) > 0 THEN TRUE ELSE FALSE END FROM MedicalRecord mr"
          + " WHERE mr.id = :recordId AND mr.pet.owner.id = :ownerId")
  boolean existsByIdAndPetOwnerId(
      @Param("recordId") Long recordId, @Param("ownerId") Long ownerId);

  @Query(
      "SELECT CASE WHEN COUNT(mr) > 0 THEN TRUE ELSE FALSE END FROM MedicalRecord mr"
          + " JOIN mr.pet p JOIN p.appointments a"
          + " WHERE mr.id = :recordId AND a.clinic.id = :clinicId")
  boolean existsByIdAndClinicId(
      @Param("recordId") Long recordId, @Param("clinicId") Long clinicId);
}
