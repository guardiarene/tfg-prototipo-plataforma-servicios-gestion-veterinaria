package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.medical.Treatment;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {}
