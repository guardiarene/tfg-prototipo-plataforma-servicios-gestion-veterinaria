package tfg.psygcv.service.interfaces;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.user.User;

public interface VisitServiceInterface {

  /**
   * Creates a new visit for an existing medical record
   *
   * @param medicalRecordId The medical record ID
   * @param visit The visit to create
   * @param veterinarian The veterinarian creating the visit
   * @return The saved visit
   */
  Visit createVisit(Long medicalRecordId, Visit visit, User veterinarian);

  /**
   * Updates an existing visit
   *
   * @param visitId The visit ID
   * @param updatedVisit The updated visit data
   * @param veterinarian The veterinarian updating the visit
   * @return The updated visit
   */
  Visit updateVisit(Long visitId, Visit updatedVisit, User veterinarian);

  /**
   * Finds a complete visit by ID (with all relations)
   *
   * @param visitId The visit ID
   * @return The complete visit
   */
  Visit findCompleteById(Long visitId);

  /**
   * Finds all visits for a medical record
   *
   * @param medicalRecordId The medical record ID
   * @return List of visits ordered by date descending
   */
  List<Visit> findByMedicalRecord(Long medicalRecordId);

  /**
   * Finds visits for a medical record with pagination
   *
   * @param medicalRecordId The medical record ID
   * @param pageable Pagination parameters
   * @return Page of visits
   */
  Page<Visit> findByMedicalRecord(Long medicalRecordId, Pageable pageable);

  /**
   * Finds visits for a medical record within a date range
   *
   * @param medicalRecordId The medical record ID
   * @param startDate Start date
   * @param endDate End date
   * @return List of visits in the date range
   */
  List<Visit> findByMedicalRecordAndDateRange(
      Long medicalRecordId, LocalDate startDate, LocalDate endDate);

  /**
   * Finds the latest visit for a medical record
   *
   * @param medicalRecordId The medical record ID
   * @return The latest visit or null if none exists
   */
  Visit findLatestVisit(Long medicalRecordId);

  /**
   * Soft deletes a visit
   *
   * @param visitId The visit ID
   * @param veterinarian The veterinarian deleting the visit
   */
  void deleteVisit(Long visitId, User veterinarian);
}
