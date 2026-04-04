package tfg.psygcv.service.clinic;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.repository.clinic.VeterinaryClinicRepository;
import tfg.psygcv.service.user.UserService;

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
  @Transactional
  public void registerStaff(Long ownerId, CreateStaffCommand command) {
    if (command.getRole() != Role.VETERINARIAN && command.getRole() != Role.RECEPTIONIST) {
      throw new IllegalArgumentException("Staff role must be VETERINARIAN or RECEPTIONIST");
    }
    VeterinaryClinic clinic = findByOwnerId(ownerId);
    User staffUser = new User();
    staffUser.setFirstName(command.getFirstName());
    staffUser.setLastName(command.getLastName());
    staffUser.setEmail(command.getEmail());
    staffUser.setPassword(command.getPassword());
    staffUser.setPhone(command.getPhone());
    staffUser.setRole(command.getRole());
    staffUser.setActive(true);
    staffUser.setWorkClinic(clinic);
    userService.save(staffUser);
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
    User owner = new User();
    owner.setFirstName(command.getUserFirstName());
    owner.setLastName(command.getUserLastName());
    owner.setEmail(command.getUserEmail());
    owner.setPassword(command.getUserPassword());
    owner.setPhone(command.getUserPhone());
    owner.setRole(Role.VETERINARIAN);
    owner.setActive(true);
    userService.save(owner);

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
