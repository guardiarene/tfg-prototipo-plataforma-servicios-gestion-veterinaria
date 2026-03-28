package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

@Component
public class PetValidator extends BaseValidator {

  public void validateVeterinarian(User veterinarian) {
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    boolean hasClinicsOwned = veterinarian.getClinicsOwned() != null && !veterinarian.getClinicsOwned().isEmpty();
    boolean hasWorkClinic = veterinarian.getWorkClinic() != null;
    if (!hasClinicsOwned && !hasWorkClinic) {
      throw new IllegalArgumentException("Veterinarian must have associated clinics");
    }
  }

  public void validateForCreation(Pet pet) {
    validateNotNull(pet, "Pet cannot be null");
    validateBasicFields(pet);
  }

  public void validateForUpdate(Pet pet) {
    validateNotNull(pet, "Pet cannot be null");
    validateBasicFields(pet);
  }

  private void validateBasicFields(Pet pet) {
    validateStringNotBlank(pet.getName(), "Pet name");
    validateNotNull(pet.getSpecies(), "Pet species cannot be null");
    validateNotNull(pet.getBreed(), "Pet breed cannot be null");
  }
}
