package tfg.psygcv.medical.visit.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.psygcv.medical.visit.command.CreateVisitCommand;
import tfg.psygcv.medical.visit.command.UpdateVisitCommand;
import tfg.psygcv.medical.visit.entity.Visit;

public interface VisitService {

  Visit createVisit(Long medicalRecordId, CreateVisitCommand command, Long veterinarianId);

  Visit updateVisit(Long visitId, UpdateVisitCommand command, Long veterinarianId);

  Visit findCompleteById(Long visitId);

  List<Visit> findByMedicalRecord(Long medicalRecordId);

  Page<Visit> findByMedicalRecord(Long medicalRecordId, Pageable pageable);

  List<Visit> findByMedicalRecordAndDateRange(
      Long medicalRecordId, LocalDate startDate, LocalDate endDate);

  Visit findLatestVisit(Long medicalRecordId);

  Long deleteVisit(Long visitId);
}
