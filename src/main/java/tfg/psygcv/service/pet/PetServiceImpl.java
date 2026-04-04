package tfg.psygcv.service.pet;

import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.repository.appointment.AppointmentQueryRepository;
import tfg.psygcv.repository.pet.PetRepository;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;
  private final UserService userService;
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
    User veterinarianWithClinicContext =
        userService.findByIdWithClinicContext(veterinarian.getId());
    petValidator.validateVeterinarian(veterinarianWithClinicContext);

    Set<VeterinaryClinic> clinics =
        new LinkedHashSet<>(veterinarianWithClinicContext.getClinicsOwned());
    if (veterinarianWithClinicContext.getWorkClinic() != null) {
      clinics.add(veterinarianWithClinicContext.getWorkClinic());
    }
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
