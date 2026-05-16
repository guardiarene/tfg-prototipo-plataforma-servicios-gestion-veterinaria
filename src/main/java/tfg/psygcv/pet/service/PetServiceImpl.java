package tfg.psygcv.pet.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.appointment.repository.AppointmentQueryRepository;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.pet.command.CreatePetCommand;
import tfg.psygcv.pet.command.UpdatePetCommand;
import tfg.psygcv.pet.entity.Pet;
import tfg.psygcv.pet.repository.PetRepository;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.service.UserService;

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
  public List<Pet> findPetsWithAppointmentsInClinics(Long veterinarianId) {
    petValidator.validateId(veterinarianId);
    User veterinarianWithClinicContext = userService.findByIdWithClinicContext(veterinarianId);

    Set<VeterinaryClinic> clinics =
        new LinkedHashSet<>(veterinarianWithClinicContext.getClinicsOwned());
    if (veterinarianWithClinicContext.getWorkClinic() != null) {
      clinics.add(veterinarianWithClinicContext.getWorkClinic());
    }
    return appointmentQueryRepository.findPetsWithAppointmentsInClinics(clinics);
  }

  @Override
  @Transactional
  public Pet save(CreatePetCommand command) {
    petValidator.validateForCreation(command);
    User owner = userService.findById(command.getOwnerId());
    Pet pet = new Pet();
    pet.setName(command.getName());
    pet.setSex(command.getSex());
    pet.setBreed(command.getBreed());
    pet.setSpecies(command.getSpecies());
    pet.setBirthDate(command.getBirthDate());
    pet.setWeight(command.getWeight());
    pet.setOwner(owner);
    return petRepository.save(pet);
  }

  @Override
  @Transactional
  public Pet update(Long petId, UpdatePetCommand command) {
    petValidator.validateId(petId);
    petValidator.validateForUpdate(command);
    Pet existingPet = findById(petId);
    existingPet.setName(command.getName());
    existingPet.setSex(command.getSex());
    existingPet.setBreed(command.getBreed());
    existingPet.setSpecies(command.getSpecies());
    existingPet.setBirthDate(command.getBirthDate());
    existingPet.setWeight(command.getWeight());
    return petRepository.save(existingPet);
  }

  @Override
  @Transactional
  public void deactivate(Long petId) {
    Pet existingPet = findById(petId);
    existingPet.setActive(false);
    petRepository.save(existingPet);
  }
}
