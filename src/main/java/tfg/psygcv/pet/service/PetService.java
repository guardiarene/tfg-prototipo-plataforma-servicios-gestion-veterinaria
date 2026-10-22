package tfg.psygcv.pet.service;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import tfg.psygcv.pet.command.CreatePetCommand;
import tfg.psygcv.pet.command.UpdatePetCommand;
import tfg.psygcv.pet.entity.Pet;

public interface PetService {

  @PreAuthorize("@authz.canAccessPet(authentication, #petId)")
  Pet findById(Long petId);

  List<Pet> findByOwnerId(Long ownerId);

  List<Pet> findPetsWithAppointmentsInClinics(Long veterinarianId);

  Pet save(CreatePetCommand command);

  @PreAuthorize("@authz.canAccessPet(authentication, #petId)")
  Pet update(Long petId, UpdatePetCommand command);

  @PreAuthorize("@authz.canAccessPet(authentication, #petId)")
  void deactivate(Long petId);
}
