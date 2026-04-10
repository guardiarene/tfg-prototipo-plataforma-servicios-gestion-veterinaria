package tfg.psygcv.medical.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.visit.entity.Treatment;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {}
