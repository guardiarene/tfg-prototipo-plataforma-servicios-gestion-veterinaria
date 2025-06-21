package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.MedicalRecord;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.pet IN (SELECT mr.pet FROM Appointment a WHERE a.clinic.id = :clinicId) AND mr.pet.active = true ORDER BY mr.date ASC")
    List<MedicalRecord> findByClinicId(@Param("clinicId") Long clinicId);

}
