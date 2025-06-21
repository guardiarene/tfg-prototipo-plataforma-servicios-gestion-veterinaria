package tfg.psygcv.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.MedicalRecord;

import java.util.Optional;

@Repository
public interface MedicalRecordQueryRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.clinicalExam LEFT JOIN FETCH mr.anamnesis WHERE mr.id = :id")
    Optional<MedicalRecord> findWithBasicRelations(@Param("id") Long id);

    @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.treatments WHERE mr.id = :id")
    Optional<MedicalRecord> findWithTreatments(@Param("id") Long id);

    @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.diagnostics WHERE mr.id = :id")
    Optional<MedicalRecord> findWithDiagnostics(@Param("id") Long id);

    @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.anamnesis a LEFT JOIN FETCH a.vaccines WHERE mr.id = :id")
    Optional<MedicalRecord> findWithVaccines(@Param("id") Long id);

    @Query("SELECT DISTINCT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.clinicalExam LEFT JOIN FETCH mr.anamnesis a LEFT JOIN FETCH a.vaccines LEFT JOIN FETCH mr.treatments LEFT JOIN FETCH mr.diagnostics WHERE mr.id = :id")
    Optional<MedicalRecord> findCompleteForEditing(@Param("id") Long id);

}
