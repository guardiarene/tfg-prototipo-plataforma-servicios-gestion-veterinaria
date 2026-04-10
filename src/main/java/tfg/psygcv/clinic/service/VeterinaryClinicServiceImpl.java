package tfg.psygcv.clinic.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.clinic.command.RegisterClinicWithVeterinarianCommand;
import tfg.psygcv.clinic.command.UpdateClinicCommand;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.clinic.repository.VeterinaryClinicRepository;
import tfg.psygcv.user.command.CreateStaffCommand;
import tfg.psygcv.user.command.RegisterVeterinarianCommand;
import tfg.psygcv.user.entity.Role;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VeterinaryClinicServiceImpl implements VeterinaryClinicService {

  private final VeterinaryClinicRepository veterinaryClinicRepository;
  private final VeterinaryClinicValidator veterinaryClinicValidator;
  private final UserService userService;

  @Override
  public List<VeterinaryClinic> findAll() {
    return veterinaryClinicRepository.findAllActive();
  }

  @Override
  public VeterinaryClinic findById(Long clinicId) {
    veterinaryClinicValidator.validateId(clinicId);
    return veterinaryClinicRepository
        .findByIdWithDetails(clinicId)
        .orElseThrow(
            () -> new EntityNotFoundException("Veterinary clinic not found with ID: " + clinicId));
  }

  @Override
  public List<VeterinaryClinic> searchByName(String query) {
    veterinaryClinicValidator.validateSearchQuery(query);
    return veterinaryClinicRepository.findByNameContainingIgnoreCase(query);
  }

  @Override
  public VeterinaryClinic findByOwnerId(Long ownerId) {
    veterinaryClinicValidator.validateId(ownerId);
    VeterinaryClinic clinic = veterinaryClinicRepository.findByOwnerId(ownerId);
    if (clinic == null) {
      throw new EntityNotFoundException("Veterinary clinic not found for owner ID: " + ownerId);
    }
    return clinic;
  }

  @Override
  public VeterinaryClinic findByVeterinarianId(Long veterinarianId) {
    veterinaryClinicValidator.validateId(veterinarianId);
    return veterinaryClinicRepository
        .findByVeterinarianId(veterinarianId)
        .or(() -> veterinaryClinicRepository.findByOwnerIdOptional(veterinarianId))
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Veterinary clinic not found for veterinarian ID: " + veterinarianId));
  }

  @Override
  public VeterinaryClinic findByReceptionistId(Long receptionistId) {
    veterinaryClinicValidator.validateId(receptionistId);
    return veterinaryClinicRepository
        .findByReceptionistId(receptionistId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Veterinary clinic not found for receptionist ID: " + receptionistId));
  }

  @Override
  public Long findClinicIdByVeterinarianId(Long veterinarianId) {
    return findByVeterinarianId(veterinarianId).getId();
  }

  @Override
  public Long findClinicIdByReceptionistId(Long receptionistId) {
    return findByReceptionistId(receptionistId).getId();
  }

  @Override
  @Transactional
  public void registerStaff(Long ownerId, CreateStaffCommand command) {
    if (command.getRole() != Role.VETERINARIAN && command.getRole() != Role.RECEPTIONIST) {
      throw new IllegalArgumentException("Staff role must be VETERINARIAN or RECEPTIONIST");
    }
    VeterinaryClinic clinic = findByOwnerId(ownerId);
    userService.registerStaffForClinic(command, clinic);
  }

  @Override
  @Transactional
  public void deactivate(Long clinicId) {
    VeterinaryClinic existingClinic = findById(clinicId);
    existingClinic.setActive(false);
    veterinaryClinicRepository.save(existingClinic);
    userService
        .findActiveByWorkClinicId(clinicId)
        .forEach(staff -> userService.deactivate(staff.getId()));
  }

  @Override
  @Transactional
  public void registerClinicWithVeterinarian(RegisterClinicWithVeterinarianCommand command) {
    User owner =
        userService.registerVeterinarian(
            RegisterVeterinarianCommand.builder()
                .firstName(command.getUserFirstName())
                .lastName(command.getUserLastName())
                .email(command.getUserEmail())
                .password(command.getUserPassword())
                .phone(command.getUserPhone())
                .build());

    VeterinaryClinic clinic = new VeterinaryClinic();
    clinic.setName(command.getClinicName());
    clinic.setAddress(command.getClinicAddress());
    clinic.setPhone(command.getClinicPhone());
    clinic.setEmail(command.getClinicEmail());
    clinic.setOwner(owner);
    clinic.setActive(true);
    veterinaryClinicValidator.validateForCreation(clinic);
    veterinaryClinicRepository.save(clinic);
  }

  @Override
  @Transactional
  public void updateClinicData(Long veterinarianId, UpdateClinicCommand command) {
    VeterinaryClinic clinic = findByOwnerId(veterinarianId);
    clinic.setName(command.getName());
    clinic.setAddress(command.getAddress());
    clinic.setPhone(command.getPhone());
    clinic.setEmail(command.getEmail());
    veterinaryClinicRepository.save(clinic);
  }
}
