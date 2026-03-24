package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.clinic.VeterinaryClinic;

@Component
public class VeterinaryClinicValidator extends BaseValidator {

  public void validateSearchQuery(String query) {
    validateStringNotBlank(query, "Search query");
  }

  public void validateForCreation(VeterinaryClinic clinic) {
    validateNotNull(clinic, "Veterinary clinic cannot be null");
    validateBasicFields(clinic);
  }

  public void validateForUpdate(VeterinaryClinic clinic) {
    validateNotNull(clinic, "Veterinary clinic cannot be null");
    validateId(clinic.getId());
    validateBasicFields(clinic);
  }

  private void validateBasicFields(VeterinaryClinic clinic) {
    validateStringNotBlank(clinic.getName(), "Clinic name");
    validateEmail(clinic.getEmail());
  }
}
