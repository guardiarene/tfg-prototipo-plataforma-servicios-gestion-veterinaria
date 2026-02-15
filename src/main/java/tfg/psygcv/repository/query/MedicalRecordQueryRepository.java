package tfg.psygcv.repository.query;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.MedicalRecord;

@Repository
public interface MedicalRecordQueryRepository extends JpaRepository<MedicalRecord, Long> {

  @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.pet WHERE mr.id = :id")
  Optional<MedicalRecord> findWithPet(@Param("id") Long id);

  @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.visits WHERE mr.id = :id")
  Optional<MedicalRecord> findWithVisits(@Param("id") Long id);

  @Query("SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.vaccines WHERE mr.id = :id")
  Optional<MedicalRecord> findWithVaccines(@Param("id") Long id);

  @Query(
      "SELECT DISTINCT mr FROM MedicalRecord mr "
          + "LEFT JOIN FETCH mr.pet "
          + "LEFT JOIN FETCH mr.visits v "
          + "LEFT JOIN FETCH v.veterinarian "
          + "WHERE mr.id = :id")
  Optional<MedicalRecord> findCompleteForViewing(@Param("id") Long id);

  @Query(
      "SELECT DISTINCT mr FROM MedicalRecord mr "
          + "LEFT JOIN FETCH mr.pet "
          + "LEFT JOIN FETCH mr.vaccines "
          + "WHERE mr.id = :id")
  Optional<MedicalRecord> findWithAllVaccines(@Param("id") Long id);

  @Query("SELECT mr FROM MedicalRecord mr WHERE mr.pet.id = :petId")
  Optional<MedicalRecord> findByPetId(@Param("petId") Long petId);
}
