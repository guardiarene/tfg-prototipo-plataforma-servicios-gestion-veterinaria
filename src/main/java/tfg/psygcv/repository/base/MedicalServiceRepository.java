package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.clinic.MedicalService;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {

    @Query("SELECT ms FROM MedicalService ms WHERE ms.id = :serviceId AND ms.active = true")
    Optional<MedicalService> findByIdAndActive(@Param("serviceId") Long serviceId);

    @Query("SELECT ms FROM MedicalService ms WHERE ms.clinic.id = :clinicId AND ms.active = true")
    List<MedicalService> findByClinicId(@Param("clinicId") Long clinicId);

}
