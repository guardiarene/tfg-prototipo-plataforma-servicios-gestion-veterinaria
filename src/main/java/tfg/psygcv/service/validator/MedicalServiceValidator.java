package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.user.User;

@Component
public class MedicalServiceValidator extends BaseValidator {

  public void validateVeterinarian(User veterinarian) {
    validateNotNull(veterinarian, "Veterinarian cannot be null");
  }

  public void validateForCreation(MedicalService service) {
    validateNotNull(service, "Medical service cannot be null");
    validateBasicFields(service);
  }

  public void validateForUpdate(MedicalService service) {
    validateNotNull(service, "Medical service cannot be null");
    validateBasicFields(service);
  }

  private void validateBasicFields(MedicalService service) {
    validateStringNotBlank(service.getName(), "Service name");
  }
}
