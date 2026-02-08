package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

public interface PetServiceInterface {

  Pet findById(Long petId);

  List<Pet> findByOwnerId(Long ownerId);

  List<Pet> findPetsWithAppointmentsInClinics(User veterinarian);

  Pet save(Pet pet, Long ownerId);

  Pet update(Long petId, Pet pet);

  void deactivate(Long petId);
}
