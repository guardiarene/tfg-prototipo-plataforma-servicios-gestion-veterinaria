package tfg.psygcv.service.interfaces;

import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

import java.util.List;

public interface PetServiceInterface {

    Pet findById(Long petId);

    List<Pet> findByOwnerId(Long ownerId);

    List<Pet> findPetsWithAppointmentsInClinics(User veterinarian);

    Pet save(Pet pet, Long ownerId);

    Pet update(Long petId, Pet pet);

    void deactivate(Long petId);

}
