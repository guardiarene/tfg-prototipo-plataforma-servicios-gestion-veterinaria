package tfg.psygcv.medical.visit.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.medical.visit.entity.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

  @Query(
      "SELECT v FROM Visit v WHERE v.medicalRecord.id = :medicalRecordId AND v.active = true ORDER BY v.date DESC, v.createdAt DESC")
  List<Visit> findByMedicalRecordIdAndActiveOrderByDateDesc(
      @Param("medicalRecordId") Long medicalRecordId);

  @Query(
      "SELECT DISTINCT v FROM Visit v "
          + "LEFT JOIN FETCH v.clinicalExam "
          + "LEFT JOIN FETCH v.anamnesis a "
          + "LEFT JOIN FETCH v.veterinarian "
          + "WHERE v.medicalRecord.id = :medicalRecordId AND v.active = true ORDER BY v.date DESC, v.createdAt DESC")
  List<Visit> findCompleteByMedicalRecordId(@Param("medicalRecordId") Long medicalRecordId);

  @Query("SELECT DISTINCT v FROM Visit v LEFT JOIN FETCH v.diagnostics WHERE v.id IN :ids")
  List<Visit> findWithDiagnosticsByIds(@Param("ids") List<Long> ids);

  @Query("SELECT DISTINCT v FROM Visit v LEFT JOIN FETCH v.treatments WHERE v.id IN :ids")
  List<Visit> findWithTreatmentsByIds(@Param("ids") List<Long> ids);

  @Query("SELECT DISTINCT v FROM Visit v LEFT JOIN FETCH v.vaccines WHERE v.id IN :ids")
  List<Visit> findWithVaccinesByIds(@Param("ids") List<Long> ids);

  @Query(
      "SELECT v FROM Visit v WHERE v.medicalRecord.id = :medicalRecordId AND v.active = true ORDER BY v.date DESC, v.createdAt DESC")
  Page<Visit> findByMedicalRecordIdAndActiveOrderByDateDesc(
      @Param("medicalRecordId") Long medicalRecordId, Pageable pageable);

  @Query(
      "SELECT v FROM Visit v WHERE v.medicalRecord.id = :medicalRecordId AND v.date BETWEEN :startDate AND :endDate AND v.active = true ORDER BY v.date DESC")
  List<Visit> findByMedicalRecordIdAndDateRange(
      @Param("medicalRecordId") Long medicalRecordId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(
      "SELECT v FROM Visit v WHERE v.medicalRecord.id = :medicalRecordId AND v.active = true ORDER BY v.date DESC, v.createdAt DESC LIMIT 1")
  Optional<Visit> findLatestByMedicalRecordId(@Param("medicalRecordId") Long medicalRecordId);

  @Query(
      "SELECT v FROM Visit v "
          + "LEFT JOIN FETCH v.clinicalExam "
          + "LEFT JOIN FETCH v.anamnesis a "
          + "WHERE v.id = :id AND v.active = true")
  Optional<Visit> findCompleteById(@Param("id") Long id);

  @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.diagnostics WHERE v.id = :id")
  List<Visit> findWithDiagnostics(@Param("id") Long id);

  @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.treatments WHERE v.id = :id")
  List<Visit> findWithTreatments(@Param("id") Long id);

  @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.vaccines WHERE v.id = :id")
  List<Visit> findWithVaccines(@Param("id") Long id);
}
