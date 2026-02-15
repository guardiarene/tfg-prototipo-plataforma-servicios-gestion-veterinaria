package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.medical.Anamnesis;
import tfg.psygcv.model.medical.ClinicalExam;
import tfg.psygcv.model.medical.Diagnostic;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.medical.Treatment;
import tfg.psygcv.model.medical.Vaccine;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.AnamnesisRepository;
import tfg.psygcv.repository.base.ClinicalExamRepository;
import tfg.psygcv.repository.base.DiagnosticRepository;
import tfg.psygcv.repository.base.MedicalRecordRepository;
import tfg.psygcv.repository.base.TreatmentRepository;
import tfg.psygcv.repository.base.VaccineRepository;
import tfg.psygcv.repository.base.VisitRepository;
import tfg.psygcv.service.interfaces.VisitServiceInterface;
import tfg.psygcv.service.validator.VisitValidator;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitServiceInterface {

  private final VisitRepository visitRepository;
  private final MedicalRecordRepository medicalRecordRepository;
  private final ClinicalExamRepository clinicalExamRepository;
  private final AnamnesisRepository anamnesisRepository;
  private final DiagnosticRepository diagnosticRepository;
  private final TreatmentRepository treatmentRepository;
  private final VaccineRepository vaccineRepository;
  private final VisitValidator visitValidator;

  @Override
  @Transactional
  public Visit createVisit(Long medicalRecordId, Visit visit, User veterinarian) {
    MedicalRecord medicalRecord =
        medicalRecordRepository
            .findById(medicalRecordId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Medical record not found with ID: " + medicalRecordId));

    Pet pet = medicalRecord.getPet();
    visitValidator.validateForCreation(visit, veterinarian, pet);

    setupVisitRelationships(visit, medicalRecord, veterinarian);
    Visit savedVisit = visitRepository.save(visit);
    saveRelatedEntities(savedVisit);

    return savedVisit;
  }

  @Override
  @Transactional
  public Visit updateVisit(Long visitId, Visit updatedVisit, User veterinarian) {
    visitValidator.validateForUpdate(visitId, updatedVisit, veterinarian);

    Visit existingVisit =
        visitRepository
            .findCompleteById(visitId)
            .orElseThrow(() -> new EntityNotFoundException("Visit not found with ID: " + visitId));

    updateVisitFields(existingVisit, updatedVisit);
    updateRelatedEntities(existingVisit, updatedVisit);

    existingVisit.setUpdatedAt(LocalDateTime.now());
    return visitRepository.save(existingVisit);
  }

  @Override
  public Visit findCompleteById(Long visitId) {
    visitValidator.validateId(visitId);
    return visitRepository
        .findCompleteById(visitId)
        .orElseThrow(() -> new EntityNotFoundException("Visit not found with ID: " + visitId));
  }

  @Override
  public List<Visit> findByMedicalRecord(Long medicalRecordId) {
    if (medicalRecordId == null || medicalRecordId <= 0) {
      throw new IllegalArgumentException("Medical record ID must be a positive number");
    }
    return visitRepository.findByMedicalRecordIdAndActiveOrderByDateDesc(medicalRecordId);
  }

  @Override
  public Page<Visit> findByMedicalRecord(Long medicalRecordId, Pageable pageable) {
    if (medicalRecordId == null || medicalRecordId <= 0) {
      throw new IllegalArgumentException("Medical record ID must be a positive number");
    }
    return visitRepository.findByMedicalRecordIdAndActiveOrderByDateDesc(
        medicalRecordId, pageable);
  }

  @Override
  public List<Visit> findByMedicalRecordAndDateRange(
      Long medicalRecordId, LocalDate startDate, LocalDate endDate) {
    if (medicalRecordId == null || medicalRecordId <= 0) {
      throw new IllegalArgumentException("Medical record ID must be a positive number");
    }
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    return visitRepository.findByMedicalRecordIdAndDateRange(medicalRecordId, startDate, endDate);
  }

  @Override
  public Visit findLatestVisit(Long medicalRecordId) {
    if (medicalRecordId == null || medicalRecordId <= 0) {
      throw new IllegalArgumentException("Medical record ID must be a positive number");
    }
    return visitRepository.findLatestByMedicalRecordId(medicalRecordId).orElse(null);
  }

  @Override
  @Transactional
  public void deleteVisit(Long visitId, User veterinarian) {
    visitValidator.validateId(visitId);

    Visit visit =
        visitRepository
            .findById(visitId)
            .orElseThrow(() -> new EntityNotFoundException("Visit not found with ID: " + visitId));

    visit.setActive(false);
    visit.setUpdatedAt(LocalDateTime.now());
    visitRepository.save(visit);
  }

  private void setupVisitRelationships(Visit visit, MedicalRecord medicalRecord, User veterinarian) {
    visit.setMedicalRecord(medicalRecord);
    visit.setVeterinarian(veterinarian);
    visit.setCreatedAt(LocalDateTime.now());
    visit.setActive(true);

    if (visit.getDate() == null) {
      visit.setDate(LocalDate.now());
    }
  }

  private void saveRelatedEntities(Visit visit) {
    saveClinicalExam(visit);
    saveAnamnesis(visit);
    saveDiagnostics(visit);
    saveTreatments(visit);
    saveVaccines(visit);
  }

  private void saveClinicalExam(Visit visit) {
    if (visit.getClinicalExam() != null) {
      ClinicalExam exam = visit.getClinicalExam();
      exam.setVisit(visit);
      exam.setCreatedAt(LocalDateTime.now());
      exam.setActive(true);
      clinicalExamRepository.save(exam);
    }
  }

  private void saveAnamnesis(Visit visit) {
    if (visit.getAnamnesis() != null) {
      Anamnesis anamnesis = visit.getAnamnesis();
      anamnesis.setVisit(visit);
      anamnesis.setCreatedAt(LocalDateTime.now());
      anamnesis.setActive(true);
      anamnesisRepository.save(anamnesis);
    }
  }

  private void saveDiagnostics(Visit visit) {
    if (visit.getDiagnostics() != null && !visit.getDiagnostics().isEmpty()) {
      visit.getDiagnostics().forEach(d -> {
        d.setVisit(visit);
        d.setCreatedAt(LocalDateTime.now());
        d.setActive(true);
      });
      diagnosticRepository.saveAll(visit.getDiagnostics());
    }
  }

  private void saveTreatments(Visit visit) {
    if (visit.getTreatments() != null && !visit.getTreatments().isEmpty()) {
      visit.getTreatments().forEach(t -> {
        t.setVisit(visit);
        t.setCreatedAt(LocalDateTime.now());
        t.setActive(true);
      });
      treatmentRepository.saveAll(visit.getTreatments());
    }
  }

  private void saveVaccines(Visit visit) {
    if (visit.getVaccines() != null && !visit.getVaccines().isEmpty()) {
      visit.getVaccines().forEach(v -> {
        v.setVisit(visit);
        v.setMedicalRecord(visit.getMedicalRecord());
        v.setCreatedAt(LocalDateTime.now());
        v.setActive(true);
      });
      vaccineRepository.saveAll(visit.getVaccines());
    }
  }

  private void updateVisitFields(Visit existing, Visit updated) {
    existing.setReasonForVisit(updated.getReasonForVisit());
    existing.setVisitType(updated.getVisitType());
    existing.setObservations(updated.getObservations());

    if (updated.getDate() != null) {
      existing.setDate(updated.getDate());
    }
  }

  private void updateRelatedEntities(Visit existing, Visit updated) {
    updateClinicalExam(existing, updated.getClinicalExam());
    updateAnamnesis(existing, updated.getAnamnesis());
    updateDiagnostics(existing, updated.getDiagnostics());
    updateTreatments(existing, updated.getTreatments());
    updateVaccines(existing, updated.getVaccines());
  }

  private void updateClinicalExam(Visit existing, ClinicalExam updatedExam) {
    if (updatedExam != null) {
      ClinicalExam existingExam = existing.getClinicalExam();
      if (existingExam == null) {
        existingExam = new ClinicalExam();
        existingExam.setCreatedAt(LocalDateTime.now());
        existingExam.setActive(true);
        existing.setClinicalExam(existingExam);
      }
      BeanUtils.copyProperties(updatedExam, existingExam, "id", "version", "createdAt", "visit");
      existingExam.setUpdatedAt(LocalDateTime.now());
    }
  }

  private void updateAnamnesis(Visit existing, Anamnesis updatedAnamnesis) {
    if (updatedAnamnesis != null) {
      Anamnesis existingAnamnesis = existing.getAnamnesis();
      if (existingAnamnesis == null) {
        existingAnamnesis = new Anamnesis();
        existingAnamnesis.setCreatedAt(LocalDateTime.now());
        existingAnamnesis.setActive(true);
        existing.setAnamnesis(existingAnamnesis);
      }
      BeanUtils.copyProperties(
          updatedAnamnesis, existingAnamnesis, "id", "version", "createdAt", "visit");
      existingAnamnesis.setUpdatedAt(LocalDateTime.now());
    }
  }

  private void updateDiagnostics(Visit existing, List<Diagnostic> newDiagnostics) {
    if (newDiagnostics == null) {
      return;
    }

    List<Long> newDiagnosticIds =
        newDiagnostics.stream().map(Diagnostic::getId).filter(Objects::nonNull).toList();

    existing
        .getDiagnostics()
        .removeIf(d -> d.getId() != null && !newDiagnosticIds.contains(d.getId()));

    newDiagnostics.forEach(
        newDiagnostic -> {
          if (newDiagnostic.getId() == null) {
            Diagnostic diagnostic = new Diagnostic();
            BeanUtils.copyProperties(newDiagnostic, diagnostic, "id", "version");
            diagnostic.setVisit(existing);
            diagnostic.setCreatedAt(LocalDateTime.now());
            diagnostic.setActive(true);
            existing.getDiagnostics().add(diagnostic);
          } else {
            Diagnostic existingDiagnostic =
                existing.getDiagnostics().stream()
                    .filter(d -> d.getId().equals(newDiagnostic.getId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));
            BeanUtils.copyProperties(
                newDiagnostic, existingDiagnostic, "id", "version", "createdAt", "visit");
            existingDiagnostic.setUpdatedAt(LocalDateTime.now());
          }
        });
  }

  private void updateTreatments(Visit existing, List<Treatment> newTreatments) {
    if (newTreatments == null) {
      return;
    }

    List<Long> newTreatmentIds =
        newTreatments.stream().map(Treatment::getId).filter(Objects::nonNull).toList();

    existing
        .getTreatments()
        .removeIf(t -> t.getId() != null && !newTreatmentIds.contains(t.getId()));

    newTreatments.forEach(
        newTreatment -> {
          if (newTreatment.getId() == null) {
            Treatment treatment = new Treatment();
            BeanUtils.copyProperties(newTreatment, treatment, "id", "version");
            treatment.setVisit(existing);
            treatment.setCreatedAt(LocalDateTime.now());
            treatment.setActive(true);
            existing.getTreatments().add(treatment);
          } else {
            Treatment existingTreatment =
                existing.getTreatments().stream()
                    .filter(t -> t.getId().equals(newTreatment.getId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Treatment not found"));
            BeanUtils.copyProperties(
                newTreatment, existingTreatment, "id", "version", "createdAt", "visit");
            existingTreatment.setUpdatedAt(LocalDateTime.now());
          }
        });
  }

  private void updateVaccines(Visit existing, List<Vaccine> newVaccines) {
    if (newVaccines == null) {
      return;
    }

    // Get all vaccines from medical record
    List<Vaccine> allVaccines = existing.getMedicalRecord().getVaccines();
    Map<Long, Vaccine> existingVaccinesMap =
        allVaccines.stream()
            .filter(v -> v.getVisit() != null && v.getVisit().getId().equals(existing.getId()))
            .collect(Collectors.toMap(Vaccine::getId, Function.identity()));

    List<Long> newVaccineIds =
        newVaccines.stream().map(Vaccine::getId).filter(Objects::nonNull).toList();

    // Remove vaccines that are not in the new list
    allVaccines.removeIf(
        v ->
            v.getVisit() != null
                && v.getVisit().getId().equals(existing.getId())
                && v.getId() != null
                && !newVaccineIds.contains(v.getId()));

    newVaccines.forEach(
        newVaccine -> {
          if (newVaccine.getId() == null) {
            Vaccine vaccine = new Vaccine();
            BeanUtils.copyProperties(newVaccine, vaccine, "id", "version");
            vaccine.setVisit(existing);
            vaccine.setMedicalRecord(existing.getMedicalRecord());
            vaccine.setCreatedAt(LocalDateTime.now());
            vaccine.setActive(true);
            allVaccines.add(vaccine);
          } else {
            Vaccine existingVaccine =
                Optional.ofNullable(existingVaccinesMap.get(newVaccine.getId()))
                    .orElseThrow(
                        () ->
                            new EntityNotFoundException(
                                "Vaccine not found with ID: " + newVaccine.getId()));
            BeanUtils.copyProperties(
                newVaccine,
                existingVaccine,
                "id",
                "version",
                "createdAt",
                "visit",
                "medicalRecord");
            existingVaccine.setUpdatedAt(LocalDateTime.now());
          }
        });
  }
}
