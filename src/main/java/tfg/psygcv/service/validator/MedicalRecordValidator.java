package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.User;

@Component
public class MedicalRecordValidator extends BaseValidator {

  public void validateVeterinarian(User veterinarian) {
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateNotNull(veterinarian.getId(), "Veterinarian must have a valid ID");
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
    validateNotNull(medicalRecord, "Medical record cannot be null");
  }

  private void validatePetForCreation(MedicalRecord medicalRecord) {
    validateNotNull(medicalRecord.getPet(), "Pet is required for medical record creation");
    validateNotNull(medicalRecord.getPet().getId(), "Pet must have a valid ID");
  }
}
