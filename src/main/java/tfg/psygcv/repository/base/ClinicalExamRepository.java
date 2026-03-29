package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.medical.ClinicalExam;

@Repository
public interface ClinicalExamRepository extends JpaRepository<ClinicalExam, Long> {}
