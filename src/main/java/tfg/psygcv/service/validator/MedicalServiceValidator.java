package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.user.User;

@Component
public class MedicalServiceValidator {

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Invalid ID: must be positive and not null");
    }
  }

  public void validateVeterinarian(User veterinarian) {
    if (veterinarian == null) {
      throw new IllegalArgumentException("Veterinarian cannot be null");
    }
  }

  public void validateForCreation(MedicalService service) {
    if (service == null) {
      throw new IllegalArgumentException("Medical service cannot be null");
    }
    validateBasicFields(service);
  }

  public void validateForUpdate(MedicalService service) {
    if (service == null) {
      throw new IllegalArgumentException("Medical service cannot be null");
    }
    validateBasicFields(service);
  }

  private void validateBasicFields(MedicalService service) {
    if (service.getName() == null || service.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Service name cannot be null or empty");
    }
  }
}
