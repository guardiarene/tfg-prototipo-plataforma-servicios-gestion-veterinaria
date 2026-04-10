package tfg.psygcv.pet.service;

import java.util.List;
import tfg.psygcv.pet.command.CreatePetCommand;
import tfg.psygcv.pet.command.UpdatePetCommand;
import tfg.psygcv.pet.entity.Pet;

public interface PetService {

  Pet findById(Long petId);

  List<Pet> findByOwnerId(Long ownerId);

  List<Pet> findPetsWithAppointmentsInClinics(Long veterinarianId);

  Pet save(CreatePetCommand command);

  Pet update(Long petId, UpdatePetCommand command);

  void deactivate(Long petId);
}
