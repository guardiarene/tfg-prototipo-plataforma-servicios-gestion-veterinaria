package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

@Component
public class PetValidator {

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Invalid ID: must be positive and not null");
    }
  }

  public void validateVeterinarian(User veterinarian) {
    if (veterinarian == null) {
      throw new IllegalArgumentException("Veterinarian cannot be null");
    }
    if (veterinarian.getClinicsOwned() == null || veterinarian.getClinicsOwned().isEmpty()) {
      throw new IllegalArgumentException("Veterinarian must have associated clinics");
    }
  }

  public void validateForCreation(Pet pet) {
    if (pet == null) {
      throw new IllegalArgumentException("Pet cannot be null");
    }
    validateBasicFields(pet);
  }

  public void validateForUpdate(Pet pet) {
    if (pet == null) {
      throw new IllegalArgumentException("Pet cannot be null");
    }
    validateBasicFields(pet);
  }

  private void validateBasicFields(Pet pet) {
    if (pet.getName() == null || pet.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Pet name cannot be null or empty");
    }
    if (pet.getSpecies() == null || pet.getSpecies().trim().isEmpty()) {
      throw new IllegalArgumentException("Pet species cannot be null or empty");
    }
  }
}
