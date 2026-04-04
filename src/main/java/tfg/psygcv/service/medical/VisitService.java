package tfg.psygcv.service.medical;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.psygcv.entity.medical.Visit;
import tfg.psygcv.entity.user.User;

public interface VisitService {

  Visit createVisit(Long medicalRecordId, CreateVisitCommand command, User veterinarian);

  Visit updateVisit(Long visitId, UpdateVisitCommand command, User veterinarian);

  Visit findCompleteById(Long visitId);

  List<Visit> findByMedicalRecord(Long medicalRecordId);

  Page<Visit> findByMedicalRecord(Long medicalRecordId, Pageable pageable);

  List<Visit> findByMedicalRecordAndDateRange(
      Long medicalRecordId, LocalDate startDate, LocalDate endDate);

  Visit findLatestVisit(Long medicalRecordId);

  void deleteVisit(Long visitId, User veterinarian);
}
