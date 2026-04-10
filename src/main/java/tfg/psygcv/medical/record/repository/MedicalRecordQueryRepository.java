package tfg.psygcv.medical.record.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.record.entity.MedicalRecord;

@Repository
public interface MedicalRecordQueryRepository extends JpaRepository<MedicalRecord, Long> {

  @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.vaccines WHERE mr.id = :id")
  List<MedicalRecord> findWithVaccines(@Param("id") Long id);

  @Query(
      "SELECT DISTINCT mr FROM MedicalRecord mr "
          + "LEFT JOIN FETCH mr.pet p "
          + "LEFT JOIN FETCH p.owner "
          + "LEFT JOIN FETCH mr.visits v "
          + "LEFT JOIN FETCH v.veterinarian "
          + "LEFT JOIN FETCH v.clinicalExam "
          + "LEFT JOIN FETCH v.anamnesis "
          + "WHERE mr.id = :id")
  Optional<MedicalRecord> findCompleteForViewing(@Param("id") Long id);
}
