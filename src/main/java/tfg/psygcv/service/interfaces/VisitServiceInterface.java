package tfg.psygcv.service.interfaces;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.user.User;

public interface VisitServiceInterface {

  Visit createVisit(Long medicalRecordId, Visit visit, User veterinarian);

  Visit updateVisit(Long visitId, Visit updatedVisit, User veterinarian);

  Visit findCompleteById(Long visitId);

  List<Visit> findByMedicalRecord(Long medicalRecordId);

  Page<Visit> findByMedicalRecord(Long medicalRecordId, Pageable pageable);

  List<Visit> findByMedicalRecordAndDateRange(
      Long medicalRecordId, LocalDate startDate, LocalDate endDate);

  Visit findLatestVisit(Long medicalRecordId);

  void deleteVisit(Long visitId, User veterinarian);
}
