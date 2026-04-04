package tfg.psygcv.service.pet;

import java.util.List;
import tfg.psygcv.entity.pet.Pet;

public interface PetService {

  Pet findById(Long petId);

  List<Pet> findByOwnerId(Long ownerId);

  List<Pet> findPetsWithAppointmentsInClinics(Long veterinarianId);

  Pet save(CreatePetCommand command);

  Pet update(Long petId, UpdatePetCommand command);

  void deactivate(Long petId);
}
