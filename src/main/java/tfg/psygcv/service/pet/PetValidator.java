package tfg.psygcv.service.pet;

import org.springframework.stereotype.Component;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.validation.BaseValidator;

@Component
public class PetValidator extends BaseValidator {

  public void validateVeterinarian(User veterinarian) {
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateNotNull(veterinarian.getId(), "Veterinarian must have a valid ID");
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
