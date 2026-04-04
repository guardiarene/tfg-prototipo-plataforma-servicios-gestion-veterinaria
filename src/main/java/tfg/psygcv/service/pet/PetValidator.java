package tfg.psygcv.service.pet;

import org.springframework.stereotype.Component;
import tfg.psygcv.service.validation.BaseValidator;

@Component
public class PetValidator extends BaseValidator {

  public void validateForCreation(CreatePetCommand command) {
    validateNotNull(command, "Pet command cannot be null");
    validateBasicFields(command.getName(), command.getSpecies(), command.getBreed());
    validateNotNull(command.getOwnerId(), "Owner ID cannot be null");
  }

  public void validateForUpdate(UpdatePetCommand command) {
    validateNotNull(command, "Pet command cannot be null");
    validateBasicFields(command.getName(), command.getSpecies(), command.getBreed());
  }

  private void validateBasicFields(String name, Object species, Object breed) {
    validateStringNotBlank(name, "Pet name");
    validateNotNull(species, "Pet species cannot be null");
    validateNotNull(breed, "Pet breed cannot be null");
  }
}
