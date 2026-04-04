package tfg.psygcv.service.medical;

import org.springframework.stereotype.Component;
import tfg.psygcv.service.validation.BaseValidator;

@Component
public class MedicalRecordValidator extends BaseValidator {

  public void validateForCreation(CreateMedicalRecordCommand command, Long veterinarianId) {
    validateNotNull(command, "Create command cannot be null");
    validateNotNull(command.getPetId(), "Pet is required for medical record creation");
    validateId(veterinarianId);
  }

  public void validateForUpdate(Long id, UpdateMedicalRecordCommand command, Long veterinarianId) {
    validateId(id);
    validateNotNull(command, "Update command cannot be null");
    validateId(veterinarianId);
  }
}
