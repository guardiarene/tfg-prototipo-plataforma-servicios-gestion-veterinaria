package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
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

    // This method now returns a MedicalRecord with its latest visit loaded for editing
    MedicalRecord medicalRecord =
        medicalRecordQueryRepository
            .findCompleteForViewing(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Medical record not found with ID: " + id));

    // Load the latest visit with all details if exists
    Visit latestVisit = visitService.findLatestVisit(id);
    if (latestVisit != null) {
      Visit completeVisit = visitService.findCompleteById(latestVisit.getId());
      // Replace with complete visit
      medicalRecord.getVisits().clear();
      medicalRecord.getVisits().add(completeVisit);
    }

    return medicalRecord;
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

    // Load all visits
    List<Visit> visits = visitService.findByMedicalRecord(medicalRecordId);
    medicalRecord.getVisits().clear();
    medicalRecord.getVisits().addAll(visits);

    return medicalRecord;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MedicalRecord> findByVeterinarian(User veterinarian) {
    medicalRecordValidator.validateVeterinarian(veterinarian);
    VeterinaryClinic clinic = veterinaryClinicRepository.findByVeterinarianId(veterinarian.getId());
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

    // Create MedicalRecord
    MedicalRecord newMedicalRecord = createMedicalRecord(pet);
    MedicalRecord savedRecord = medicalRecordRepository.save(newMedicalRecord);

    // Create first Visit from the medicalRecord data (backward compatibility)
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

    // Update general observations if provided
    if (updatedRecord.getGeneralObservations() != null) {
      existingRecord.setGeneralObservations(updatedRecord.getGeneralObservations());
    }

    existingRecord.setUpdatedAt(LocalDateTime.now());

    return medicalRecordRepository.save(existingRecord);
  }

  private MedicalRecord createMedicalRecord(Pet pet) {
    MedicalRecord medicalRecord = new MedicalRecord();
    medicalRecord.setPet(pet);
    medicalRecord.setCreatedAt(LocalDateTime.now());
    medicalRecord.setActive(true);
    pet.setMedicalRecord(medicalRecord);
    return medicalRecord;
  }

  private Visit createFirstVisitFromMedicalRecord(MedicalRecord medicalRecord) {
    Visit visit = new Visit();

    // Extract visit data from MedicalRecord (backward compatibility)
    // The old MedicalRecord had these fields directly, now they go into the first Visit
    visit.setReasonForVisit(
        medicalRecord.getGeneralObservations() != null
            ? medicalRecord.getGeneralObservations()
            : "Primera visita");
    visit.setVisitType(
        tfg.psygcv.model.medical.VisitType.CONSULTATION); // Default type for backward compatibility
    visit.setDate(java.time.LocalDate.now());

    // Transfer related entities to the visit (backward compatibility)
    if (medicalRecord.getVisits() != null && !medicalRecord.getVisits().isEmpty()) {
      Visit sourceVisit = medicalRecord.getVisits().get(0);
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
    List<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
    boolean hasAppointments =
        appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
            pet, veterinarianClinics, pet.getOwner());

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
