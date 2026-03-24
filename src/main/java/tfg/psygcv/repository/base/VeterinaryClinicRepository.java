package tfg.psygcv.repository.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.clinic.VeterinaryClinic;

@Repository
public interface VeterinaryClinicRepository extends JpaRepository<VeterinaryClinic, Long> {

  @Query(
      "SELECT DISTINCT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.services s WHERE vc.active = true AND (s.active = true OR s IS NULL)")
  List<VeterinaryClinic> findAllActive();

  @Query(
      "SELECT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.services s LEFT JOIN FETCH vc.owner o WHERE vc.id = :clinicId AND (s.active IS NULL OR s.active = true)")
  Optional<VeterinaryClinic> findByIdWithDetails(@Param("clinicId") Long clinicId);

  List<VeterinaryClinic> findByNameContainingIgnoreCase(String name);

  @Query("SELECT vc FROM VeterinaryClinic vc WHERE vc.owner.id = :ownerId")
  VeterinaryClinic findByOwnerId(@Param("ownerId") Long ownerId);

  @Query(
      "SELECT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.veterinarians v LEFT JOIN FETCH vc.receptionists r WHERE vc.owner.id = :ownerId")
  Optional<VeterinaryClinic> findByOwnerIdOptional(@Param("ownerId") Long ownerId);

  @Query(
      "SELECT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.veterinarians v LEFT JOIN FETCH vc.receptionists r JOIN vc.veterinarians v2 WHERE v2.id = :veterinarianId")
  Optional<VeterinaryClinic> findByVeterinarianId(@Param("veterinarianId") Long veterinarianId);

  @Query("SELECT vc FROM VeterinaryClinic vc JOIN vc.receptionists r WHERE r.id = :receptionistId")
  Optional<VeterinaryClinic> findByReceptionistId(@Param("receptionistId") Long receptionistId);
}
