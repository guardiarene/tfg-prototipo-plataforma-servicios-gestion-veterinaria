package tfg.psygcv.clinic.service;

import org.springframework.stereotype.Component;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.shared.validation.BaseValidator;

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
