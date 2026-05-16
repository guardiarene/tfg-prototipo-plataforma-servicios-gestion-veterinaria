package tfg.psygcv.medical.visit.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.visit.entity.Diagnostic;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Long> {

  @Query("SELECT DISTINCT d FROM Diagnostic d LEFT JOIN FETCH d.problems WHERE d.id IN :ids")
  List<Diagnostic> findWithProblemsByIds(@Param("ids") List<Long> ids);
}
