package tfg.psygcv.service.pet;

import java.util.List;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;

public interface PetService {

  Pet findById(Long petId);

  List<Pet> findByOwnerId(Long ownerId);

  List<Pet> findPetsWithAppointmentsInClinics(User veterinarian);

  Pet save(Pet pet, Long ownerId);

  Pet update(Long petId, Pet pet);

  void deactivate(Long petId);
}
