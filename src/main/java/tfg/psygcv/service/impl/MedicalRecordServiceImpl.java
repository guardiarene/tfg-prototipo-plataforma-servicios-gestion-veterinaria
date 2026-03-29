package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.medical.Anamnesis;
import tfg.psygcv.entity.medical.ClinicalExam;
import tfg.psygcv.entity.medical.Diagnostic;
import tfg.psygcv.entity.medical.MedicalRecord;
import tfg.psygcv.entity.medical.Treatment;
import tfg.psygcv.entity.medical.Vaccine;
import tfg.psygcv.entity.medical.Visit;
import tfg.psygcv.entity.medical.VisitType;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.repository.base.MedicalRecordRepository;
import tfg.psygcv.repository.base.PetRepository;
import tfg.psygcv.repository.base.VeterinaryClinicRepository;
import tfg.psygcv.repository.query.AppointmentQueryRepository;
import tfg.psygcv.repository.query.MedicalRecordQueryRepository;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.interfaces.VisitServiceInterface;
import tfg.psygcv.service.validator.MedicalRecordValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicalRecordServiceImpl implements MedicalRecordServiceInterface {

  private final MedicalRecordRepository medicalRecordRepository;
  private final MedicalRecordQueryRepository medicalRecordQueryRepository;
  private final VeterinaryClinicRepository veterinaryClinicRepository;
  private final PetRepository petRepository;
  private final AppointmentQueryRepository appointmentQueryRepository;
  private final VisitServiceInterface visitService;
  private final MedicalRecordValidator medicalRecordValidator;

  @Override
  @Transactional(readOnly = true)
  public MedicalRecord findCompleteForEditing(Long id) {
    medicalRecordValidator.validateId(id);
    MedicalRecord medicalRecord =
        medicalRecordQueryRepository
            .findCompleteForViewing(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Medical record not found with ID: " + id));
    Visit latestVisit = visitService.findLatestVisit(id);
    Visit newVisit = new Visit();
    newVisit.setDate(java.time.LocalDate.now());
    newVisit.setVisitType(VisitType.CONSULTATION);
    if (latestVisit != null) {
      Visit completeVisit = visitService.findCompleteById(latestVisit.getId());
      newVisit.setReasonForVisit(completeVisit.getReasonForVisit());
      newVisit.setObservations(completeVisit.getObservations());
      if (completeVisit.getClinicalExam() != null) {
        ClinicalExam newExam = new ClinicalExam();
        BeanUtils.copyProperties(completeVisit.getClinicalExam(), newExam, "id", "visit");
        newVisit.setClinicalExam(newExam);
      }
      if (completeVisit.getAnamnesis() != null) {
        Anamnesis newAnamnesis = new Anamnesis();
        BeanUtils.copyProperties(completeVisit.getAnamnesis(), newAnamnesis, "id", "visit");
        newVisit.setAnamnesis(newAnamnesis);
      }
      if (completeVisit.getDiagnostics() != null) {
        for (Diagnostic d : completeVisit.getDiagnostics()) {
          Diagnostic newD = new Diagnostic();
          newD.setProblems(new ArrayList<>(d.getProblems()));
          newVisit.getDiagnostics().add(newD);
        }
      }
      if (completeVisit.getTreatments() != null) {
        for (Treatment t : completeVisit.getTreatments()) {
          Treatment newT = new Treatment();
          BeanUtils.copyProperties(t, newT, "id", "visit");
          newVisit.getTreatments().add(newT);
        }
      }
      if (completeVisit.getVaccines() != null) {
        for (Vaccine v : completeVisit.getVaccines()) {
          Vaccine newV = new Vaccine();
          BeanUtils.copyProperties(v, newV, "id", "visit", "medicalRecord");
          newVisit.getVaccines().add(newV);
        }
      }
    }
    MedicalRecord recordForForm = new MedicalRecord();
    BeanUtils.copyProperties(medicalRecord, recordForForm, "visits");
    recordForForm.getVisits().add(newVisit);
    return recordForForm;
  }

  @Override
  @Transactional(readOnly = true)
  public MedicalRecord findCompleteById(Long medicalRecordId) {
    medicalRecordValidator.validateId(medicalRecordId);
    MedicalRecord medicalRecord =
        medicalRecordQueryRepository
            .findCompleteForViewing(medicalRecordId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Medical record not found with ID: " + medicalRecordId));
    List<Visit> visits = visitService.findByMedicalRecord(medicalRecordId);
    medicalRecord.getVisits().clear();
    medicalRecord.getVisits().addAll(visits);
    medicalRecordQueryRepository.findWithVaccines(medicalRecordId);
    return medicalRecord;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MedicalRecord> findByVeterinarian(User veterinarian) {
    medicalRecordValidator.validateVeterinarian(veterinarian);
    VeterinaryClinic clinic =
        veterinaryClinicRepository
            .findByVeterinarianId(veterinarian.getId())
            .or(() -> veterinaryClinicRepository.findByOwnerIdOptional(veterinarian.getId()))
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Veterinary clinic not found for veterinarian ID: "
                            + veterinarian.getId()));

    return medicalRecordRepository.findByClinicId(clinic.getId());
  }

  @Override
  @Transactional
  public MedicalRecord save(MedicalRecord medicalRecord, User veterinarian) {
    medicalRecordValidator.validateForCreation(medicalRecord, veterinarian);
    Pet pet =
        petRepository
            .findByIdAndActive(medicalRecord.getPet().getId())
            .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
    validatePetAppointments(pet, veterinarian);
    validateExistingMedicalRecord(pet);
    MedicalRecord newMedicalRecord = createMedicalRecord(pet);
    MedicalRecord savedRecord = medicalRecordRepository.save(newMedicalRecord);
    Visit firstVisit = createFirstVisitFromMedicalRecord(medicalRecord);
    visitService.createVisit(savedRecord.getId(), firstVisit, veterinarian);
    return savedRecord;
  }

  @Override
  @Transactional
  public MedicalRecord update(Long id, MedicalRecord updatedRecord, User veterinarian) {
    medicalRecordValidator.validateForUpdate(id, updatedRecord, veterinarian);
    MedicalRecord existingRecord =
        medicalRecordRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Medical record not found"));
    if (updatedRecord.getGeneralObservations() != null) {
      existingRecord.setGeneralObservations(updatedRecord.getGeneralObservations());
    }
    if (updatedRecord.getVisits() != null && !updatedRecord.getVisits().isEmpty()) {
      for (Visit updatedVisit : updatedRecord.getVisits()) {
        updatedVisit.setMedicalRecord(null);
        if (updatedVisit.getClinicalExam() != null) {
          updatedVisit.getClinicalExam().setVisit(updatedVisit);
        }
        if (updatedVisit.getAnamnesis() != null) {
          updatedVisit.getAnamnesis().setVisit(updatedVisit);
        }
        if (updatedVisit.getDiagnostics() != null) {
          updatedVisit.getDiagnostics().forEach(d -> d.setVisit(updatedVisit));
        }
        if (updatedVisit.getTreatments() != null) {
          updatedVisit.getTreatments().forEach(t -> t.setVisit(updatedVisit));
        }
        if (updatedVisit.getVaccines() != null) {
          updatedVisit.getVaccines().forEach(vax -> vax.setVisit(updatedVisit));
        }
        if (updatedVisit.getId() != null) {
          visitService.updateVisit(updatedVisit.getId(), updatedVisit, veterinarian);
        } else {
          visitService.createVisit(existingRecord.getId(), updatedVisit, veterinarian);
        }
      }
    }
    return medicalRecordRepository.save(existingRecord);
  }

  private MedicalRecord createMedicalRecord(Pet pet) {
    MedicalRecord medicalRecord = new MedicalRecord();
    medicalRecord.setPet(pet);
    medicalRecord.setActive(true);
    pet.setMedicalRecord(medicalRecord);
    return medicalRecord;
  }

  private Visit createFirstVisitFromMedicalRecord(MedicalRecord medicalRecord) {
    Visit visit = new Visit();
    visit.setReasonForVisit(
        medicalRecord.getGeneralObservations() != null
            ? medicalRecord.getGeneralObservations()
            : "Primera visita");
    visit.setVisitType(tfg.psygcv.entity.medical.VisitType.CONSULTATION);
    visit.setDate(java.time.LocalDate.now());
    if (medicalRecord.getVisits() != null && !medicalRecord.getVisits().isEmpty()) {
      Visit sourceVisit = medicalRecord.getVisits().getFirst();
      visit.setClinicalExam(sourceVisit.getClinicalExam());
      visit.setAnamnesis(sourceVisit.getAnamnesis());
      visit.setDiagnostics(sourceVisit.getDiagnostics());
      visit.setTreatments(sourceVisit.getTreatments());
      visit.setVaccines(sourceVisit.getVaccines());
      visit.setObservations(sourceVisit.getObservations());
      if (sourceVisit.getReasonForVisit() != null) {
        visit.setReasonForVisit(sourceVisit.getReasonForVisit());
      }
      if (sourceVisit.getVisitType() != null) {
        visit.setVisitType(sourceVisit.getVisitType());
      }
      if (sourceVisit.getDate() != null) {
        visit.setDate(sourceVisit.getDate());
      }
    }
    return visit;
  }

  private void validatePetAppointments(Pet pet, User veterinarian) {
    Set<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
    VeterinaryClinic workClinic = veterinarian.getWorkClinic();
    boolean hasAppointments = false;
    if (veterinarianClinics != null && !veterinarianClinics.isEmpty()) {
      hasAppointments =
          appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
              pet, veterinarianClinics, pet.getOwner());
    }
    if (!hasAppointments && workClinic != null) {
      hasAppointments =
          appointmentQueryRepository.existsAppointmentByPetAndClinicAndCustomer(
              pet, workClinic, pet.getOwner());
    }
    if (!hasAppointments) {
      throw new IllegalStateException(
          "Pet has no registered appointments in veterinarian's clinics");
    }
  }

  private void validateExistingMedicalRecord(Pet pet) {
    if (pet.getMedicalRecord() != null) {
      throw new IllegalStateException("Pet already has a medical record");
    }
  }
}
