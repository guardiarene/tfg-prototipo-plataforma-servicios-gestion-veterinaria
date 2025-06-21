package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.clinic.VeterinaryClinic;

@Component
public class VeterinaryClinicValidator {

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be positive and not null");
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be null or empty");
        }
    }

    public void validateForCreation(VeterinaryClinic clinic) {
        if (clinic == null) {
            throw new IllegalArgumentException("Veterinary clinic cannot be null");
        }
        validateBasicFields(clinic);
    }

    public void validateForUpdate(VeterinaryClinic clinic) {
        if (clinic == null) {
            throw new IllegalArgumentException("Veterinary clinic cannot be null");
        }
        validateId(clinic.getId());
        validateBasicFields(clinic);
    }

    private void validateBasicFields(VeterinaryClinic clinic) {
        if (clinic.getName() == null || clinic.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Clinic name cannot be null or empty");
        }
        if (clinic.getEmail() == null || !isValidEmail(clinic.getEmail())) {
            throw new IllegalArgumentException("Valid email is required");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

}
