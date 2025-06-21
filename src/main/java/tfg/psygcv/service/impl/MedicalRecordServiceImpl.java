package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.medical.*;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.*;
import tfg.psygcv.repository.query.AppointmentQueryRepository;
import tfg.psygcv.repository.query.MedicalRecordQueryRepository;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.validator.MedicalRecordValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicalRecordServiceImpl implements MedicalRecordServiceInterface {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordQueryRepository medicalRecordQueryRepository;
    private final VeterinaryClinicRepository veterinaryClinicRepository;
    private final PetRepository petRepository;
    private final AppointmentQueryRepository appointmentQueryRepository;
    private final ClinicalExamRepository clinicalExamRepository;
    private final AnamnesisRepository anamnesisRepository;
    private final TreatmentRepository treatmentRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final MedicalRecordValidator medicalRecordValidator;

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord findCompleteForEditing(Long id) {
        medicalRecordValidator.validateId(id);

        // Try to find complete record first, fallback to building it
        Optional<MedicalRecord> complete = medicalRecordQueryRepository.findCompleteForEditing(id);
        if (complete.isPresent()) {
            return complete.get();
        }

        // Fallback: build the complete record manually
        MedicalRecord medicalRecord = medicalRecordQueryRepository.findWithBasicRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with ID: " + id));

        loadRelatedEntities(medicalRecord, id);
        return medicalRecord;
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord findCompleteById(Long clinicId) {
        medicalRecordValidator.validateId(clinicId);

        MedicalRecord recordWithVaccines = medicalRecordQueryRepository.findWithVaccines(clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with ID: " + clinicId));

        MedicalRecord recordWithTreatments = medicalRecordQueryRepository.findWithTreatments(clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with ID: " + clinicId));

        recordWithVaccines.setTreatments(recordWithTreatments.getTreatments());
        return recordWithVaccines;
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

        Pet pet = petRepository.findByIdAndActive(medicalRecord.getPet().getId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        validatePetAppointments(pet, veterinarian);
        validateExistingMedicalRecord(pet);

        setupBaseRelationships(medicalRecord, veterinarian, pet);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        saveRelatedEntities(savedRecord);

        return savedRecord;
    }

    @Override
    @Transactional
    public MedicalRecord update(Long id, MedicalRecord updatedRecord, User veterinarian) {
        medicalRecordValidator.validateForUpdate(id, updatedRecord, veterinarian);

        MedicalRecord existingRecord = medicalRecordQueryRepository.findWithBasicRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found"));

        existingRecord.setLastVeterinarian(veterinarian);
        loadRelatedEntities(existingRecord, id);

        updateMedicalRecordFields(existingRecord, updatedRecord);
        updateRelatedEntities(existingRecord, updatedRecord);

        return medicalRecordRepository.save(existingRecord);
    }

    private void loadRelatedEntities(MedicalRecord medicalRecord, Long id) {
        medicalRecordQueryRepository.findWithTreatments(id)
                .ifPresent(h -> medicalRecord.setTreatments(h.getTreatments()));
        medicalRecordQueryRepository.findWithDiagnostics(id)
                .ifPresent(h -> medicalRecord.setDiagnostics(h.getDiagnostics()));
        medicalRecordQueryRepository.findWithVaccines(id)
                .ifPresent(h -> medicalRecord.getAnamnesis().setVaccines(h.getAnamnesis().getVaccines()));
    }

    private void setupBaseRelationships(MedicalRecord medicalRecord, User veterinarian, Pet pet) {
        medicalRecord.setDate(LocalDate.now());
        medicalRecord.setVeterinarian(veterinarian);
        medicalRecord.setLastVeterinarian(veterinarian);
        medicalRecord.setOwner(pet.getOwner());
        pet.setMedicalRecord(medicalRecord);
        medicalRecord.setPet(pet);
    }

    private void saveRelatedEntities(MedicalRecord medicalRecord) {
        saveClinicalExam(medicalRecord);
        saveAnamnesis(medicalRecord);
        saveDiagnostics(medicalRecord);
        saveTreatments(medicalRecord);
    }

    private void saveClinicalExam(MedicalRecord medicalRecord) {
        if (medicalRecord.getClinicalExam() != null) {
            medicalRecord.getClinicalExam().setMedicalRecord(medicalRecord);
            clinicalExamRepository.save(medicalRecord.getClinicalExam());
        }
    }

    private void saveAnamnesis(MedicalRecord medicalRecord) {
        if (medicalRecord.getAnamnesis() != null) {
            Anamnesis anamnesis = medicalRecord.getAnamnesis();
            anamnesis.setMedicalRecord(medicalRecord);
            anamnesis.getVaccines().forEach(v -> v.setAnamnesis(anamnesis));
            anamnesisRepository.save(anamnesis);
        }
    }

    private void saveDiagnostics(MedicalRecord medicalRecord) {
        if (medicalRecord.getDiagnostics() != null) {
            medicalRecord.getDiagnostics().forEach(d -> d.setMedicalRecord(medicalRecord));
            diagnosticRepository.saveAll(medicalRecord.getDiagnostics());
        }
    }

    private void saveTreatments(MedicalRecord medicalRecord) {
        if (medicalRecord.getTreatments() != null) {
            List<Treatment> treatments = medicalRecord.getTreatments().stream()
                    .peek(t -> t.setMedicalRecord(medicalRecord))
                    .toList();
            treatmentRepository.saveAll(treatments);
        }
    }

    private void updateMedicalRecordFields(MedicalRecord existing, MedicalRecord updated) {
        existing.setReasonForVisit(updated.getReasonForVisit());
        existing.setDate(LocalDate.now());
    }

    private void updateRelatedEntities(MedicalRecord existing, MedicalRecord updated) {
        updateClinicalExam(existing, updated.getClinicalExam());
        updateAnamnesis(existing, updated.getAnamnesis());
        updateTreatments(existing, updated.getTreatments());
        updateDiagnostics(existing, updated.getDiagnostics());
    }

    private void updateClinicalExam(MedicalRecord existing, ClinicalExam updatedExam) {
        if (updatedExam != null) {
            ClinicalExam existingExam = existing.getClinicalExam();
            if (existingExam == null) {
                existingExam = new ClinicalExam();
                existing.setClinicalExam(existingExam);
            }
            BeanUtils.copyProperties(updatedExam, existingExam, "id", "version");
        }
    }

    private void updateAnamnesis(MedicalRecord existing, Anamnesis updatedAnamnesis) {
        if (updatedAnamnesis != null) {
            Anamnesis existingAnamnesis = existing.getAnamnesis();
            if (existingAnamnesis == null) {
                existingAnamnesis = new Anamnesis();
                existing.setAnamnesis(existingAnamnesis);
            }
            BeanUtils.copyProperties(updatedAnamnesis, existingAnamnesis, "id", "version", "vaccines", "medicalRecord");
            updateVaccines(existingAnamnesis, updatedAnamnesis.getVaccines());
        }
    }

    private void updateVaccines(Anamnesis existingAnamnesis, List<Vaccine> newVaccines) {
        Map<Long, Vaccine> existingVaccines = existingAnamnesis.getVaccines().stream()
                .collect(Collectors.toMap(Vaccine::getId, Function.identity()));

        List<Vaccine> updatedVaccines = newVaccines.stream().map(newVaccine -> {
            if (newVaccine.getId() == null) {
                Vaccine vaccine = new Vaccine();
                BeanUtils.copyProperties(newVaccine, vaccine, "id", "version");
                vaccine.setAnamnesis(existingAnamnesis);
                return vaccine;
            } else {
                Vaccine existingVaccine = Optional.ofNullable(existingVaccines.get(newVaccine.getId()))
                        .orElseThrow(() -> new EntityNotFoundException("Vaccine not found with ID: " + newVaccine.getId()));
                BeanUtils.copyProperties(newVaccine, existingVaccine, "id", "version", "anamnesis");
                return existingVaccine;
            }
        }).toList();

        existingAnamnesis.getVaccines().clear();
        existingAnamnesis.getVaccines().addAll(updatedVaccines);
    }

    private void updateTreatments(MedicalRecord existing, List<Treatment> newTreatments) {
        List<Long> newTreatmentIds = newTreatments.stream()
                .map(Treatment::getId)
                .filter(Objects::nonNull)
                .toList();

        existing.getTreatments().removeIf(t ->
                t.getId() != null && !newTreatmentIds.contains(t.getId()));

        newTreatments.forEach(newTreatment -> {
            if (newTreatment.getId() == null) {
                Treatment treatment = new Treatment();
                BeanUtils.copyProperties(newTreatment, treatment, "id", "version");
                treatment.setMedicalRecord(existing);
                existing.getTreatments().add(treatment);
            } else {
                Treatment existingTreatment = existing.getTreatments().stream()
                        .filter(t -> t.getId().equals(newTreatment.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Treatment not found"));
                BeanUtils.copyProperties(newTreatment, existingTreatment, "id", "version", "medicalRecord");
            }
        });
    }

    private void updateDiagnostics(MedicalRecord existing, List<Diagnostic> newDiagnostics) {
        List<Long> newDiagnosticIds = newDiagnostics.stream()
                .map(Diagnostic::getId)
                .filter(Objects::nonNull)
                .toList();

        existing.getDiagnostics().removeIf(d ->
                d.getId() != null && !newDiagnosticIds.contains(d.getId()));

        newDiagnostics.forEach(newDiagnostic -> {
            if (newDiagnostic.getId() == null) {
                Diagnostic diagnostic = new Diagnostic();
                BeanUtils.copyProperties(newDiagnostic, diagnostic, "id", "version");
                diagnostic.setMedicalRecord(existing);
                existing.getDiagnostics().add(diagnostic);
            } else {
                Diagnostic existingDiagnostic = existing.getDiagnostics().stream()
                        .filter(d -> d.getId().equals(newDiagnostic.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));
                BeanUtils.copyProperties(newDiagnostic, existingDiagnostic, "id", "version", "medicalRecord");
            }
        });
    }

    private void validatePetAppointments(Pet pet, User veterinarian) {
        List<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
        boolean hasAppointments = appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
                pet, veterinarianClinics, pet.getOwner());

        if (!hasAppointments) {
            throw new IllegalStateException("Pet has no registered appointments in veterinarian's clinics");
        }
    }

    private void validateExistingMedicalRecord(Pet pet) {
        if (pet.getMedicalRecord() != null) {
            throw new IllegalStateException("Pet already has a medical record");
        }
    }

}
