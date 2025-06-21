package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.clinic.VeterinaryClinic;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinaryClinicRepository extends JpaRepository<VeterinaryClinic, Long> {

    @Query("SELECT DISTINCT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.services s WHERE vc.active = true AND (s.active = true OR s IS NULL)")
    List<VeterinaryClinic> findAllActive();

    @Query("SELECT vc FROM VeterinaryClinic vc LEFT JOIN FETCH vc.services s WHERE vc.id = :clinicId AND (s.active IS NULL OR s.active = true)")
    Optional<VeterinaryClinic> findByIdAndActive(@Param("clinicId") Long clinicId);

    List<VeterinaryClinic> findByNameContainingIgnoreCase(String name);

    @Query("SELECT vc FROM VeterinaryClinic vc WHERE vc.veterinarian.id = :veterinarianId")
    VeterinaryClinic findByVeterinarianId(@Param("veterinarianId") Long veterinarianId);

    Optional<VeterinaryClinic> findByReceptionistId(Long receptionistId);

}
