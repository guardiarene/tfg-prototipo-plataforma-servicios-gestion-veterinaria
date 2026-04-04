package tfg.psygcv.repository.medical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.medical.Treatment;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {}
