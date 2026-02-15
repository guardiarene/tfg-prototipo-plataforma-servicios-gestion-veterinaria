package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.User;

@Component
public class MedicalRecordValidator {

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Invalid ID: must be positive and not null");
    }
  }

  public void validateVeterinarian(User veterinarian) {
    if (veterinarian == null) {
      throw new IllegalArgumentException("Veterinarian cannot be null");
    }
    if (veterinarian.getId() == null) {
      throw new IllegalArgumentException("Veterinarian must have a valid ID");
    }
  }

  public void validateForCreation(MedicalRecord medicalRecord, User veterinarian) {
    validateMedicalRecord(medicalRecord);
    validateVeterinarian(veterinarian);
    validatePetForCreation(medicalRecord);
  }

  public void validateForUpdate(Long id, MedicalRecord medicalRecord, User veterinarian) {
    validateId(id);
    validateMedicalRecord(medicalRecord);
    validateVeterinarian(veterinarian);
  }

  private void validateMedicalRecord(MedicalRecord medicalRecord) {
    if (medicalRecord == null) {
      throw new IllegalArgumentException("Medical record cannot be null");
    }
    /*if (medicalRecord.getReasonForVisit() == null
        || medicalRecord.getReasonForVisit().trim().isEmpty()) {
      throw new IllegalArgumentException("Reason for visit is required");
    }*/
  }

  private void validatePetForCreation(MedicalRecord medicalRecord) {
    if (medicalRecord.getPet() == null) {
      throw new IllegalArgumentException("Pet is required for medical record creation");
    }
    if (medicalRecord.getPet().getId() == null) {
      throw new IllegalArgumentException("Pet must have a valid ID");
    }
  }
}
