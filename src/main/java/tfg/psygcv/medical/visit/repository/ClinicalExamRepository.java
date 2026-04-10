package tfg.psygcv.medical.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.visit.entity.ClinicalExam;

@Repository
public interface ClinicalExamRepository extends JpaRepository<ClinicalExam, Long> {}
