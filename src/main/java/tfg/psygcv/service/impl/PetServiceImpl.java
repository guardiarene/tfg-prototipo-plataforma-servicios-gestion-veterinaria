package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.PetRepository;
import tfg.psygcv.repository.query.AppointmentQueryRepository;
import tfg.psygcv.service.interfaces.PetServiceInterface;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.validator.PetValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetServiceImpl implements PetServiceInterface {

  private final PetRepository petRepository;

  private final UserServiceInterface userService;

  private final AppointmentQueryRepository appointmentQueryRepository;

  private final PetValidator petValidator;

  @Override
  public Pet findById(Long petId) {
    petValidator.validateId(petId);
    return petRepository
        .findByIdAndActive(petId)
        .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + petId));
  }

  @Override
  public List<Pet> findByOwnerId(Long ownerId) {
    petValidator.validateId(ownerId);
    return petRepository.findByOwnerId(ownerId);
  }

  @Override
  public List<Pet> findPetsWithAppointmentsInClinics(User veterinarian) {
    petValidator.validateVeterinarian(veterinarian);
    List<VeterinaryClinic> clinics = veterinarian.getClinicsOwned();
    return appointmentQueryRepository.findPetsWithAppointmentsInClinics(clinics);
  }

  @Override
  @Transactional
  public Pet save(Pet pet, Long ownerId) {
    petValidator.validateForCreation(pet);
    petValidator.validateId(ownerId);
    User owner = userService.findById(ownerId);
    pet.setOwner(owner);
    return petRepository.save(pet);
  }

  @Override
  @Transactional
  public Pet update(Long petId, Pet pet) {
    petValidator.validateId(petId);
    petValidator.validateForUpdate(pet);
    Pet existingPet = findById(petId);
    updatePetFields(existingPet, pet);
    return petRepository.save(existingPet);
  }

  @Override
  @Transactional
  public void deactivate(Long petId) {
    Pet existingPet = findById(petId);
    existingPet.setActive(false);
    petRepository.save(existingPet);
  }

  private void updatePetFields(Pet existing, Pet updated) {
    existing.setName(updated.getName());
    existing.setSex(updated.getSex());
    existing.setBreed(updated.getBreed());
    existing.setSpecies(updated.getSpecies());
    existing.setBirthDate(updated.getBirthDate());
    existing.setWeight(updated.getWeight());
  }
}
