package tfg.psygcv.clinic.service;

import org.springframework.stereotype.Component;
import tfg.psygcv.clinic.command.CreateMedicalServiceCommand;
import tfg.psygcv.clinic.command.UpdateMedicalServiceCommand;
import tfg.psygcv.shared.validation.BaseValidator;

@Component
public class MedicalServiceValidator extends BaseValidator {

  public void validateForCreation(CreateMedicalServiceCommand command) {
    validateNotNull(command, "Medical service command cannot be null");
    validateStringNotBlank(command.getName(), "Service name");
    validateNotNull(command.getClinicId(), "Clinic ID cannot be null");
  }

  public void validateForUpdate(UpdateMedicalServiceCommand command) {
    validateNotNull(command, "Medical service command cannot be null");
    validateStringNotBlank(command.getName(), "Service name");
  }
}
