package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.VeterinaryClinicRepository;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;
import tfg.psygcv.service.validator.VeterinaryClinicValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VeterinaryClinicServiceImpl implements VeterinaryClinicServiceInterface {

  private final VeterinaryClinicRepository veterinaryClinicRepository;
  private final VeterinaryClinicValidator veterinaryClinicValidator;
  private final UserServiceInterface userService;

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
  public VeterinaryClinic findByVeterinarianId(Long veterinarianId) {
    veterinaryClinicValidator.validateId(veterinarianId);
    VeterinaryClinic clinic = veterinaryClinicRepository.findByVeterinarianId(veterinarianId);
    if (clinic == null) {
      throw new EntityNotFoundException(
          "Veterinary clinic not found for veterinarian ID: " + veterinarianId);
    }
    return clinic;
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
  public VeterinaryClinic save(VeterinaryClinic veterinaryClinic) {
    veterinaryClinicValidator.validateForCreation(veterinaryClinic);
    return veterinaryClinicRepository.save(veterinaryClinic);
  }

  @Override
  @Transactional
  public VeterinaryClinic update(VeterinaryClinic veterinaryClinic) {
    veterinaryClinicValidator.validateForUpdate(veterinaryClinic);
    VeterinaryClinic existingClinic = findById(veterinaryClinic.getId());
    updateClinicFields(existingClinic, veterinaryClinic);
    return veterinaryClinicRepository.save(existingClinic);
  }

  @Override
  @Transactional
  public void deactivate(Long clinicId) {
    VeterinaryClinic existingClinic = findById(clinicId);
    existingClinic.setActive(false);
    veterinaryClinicRepository.save(existingClinic);
  }

  @Override
  @Transactional
  public void registerClinicWithVeterinarian(Map<String, String> params) {
    String password = params.get("userPassword");
    String confirmPassword = params.get("confirmPassword");

    User user = new User();
    user.setFirstName(params.get("userFirstName"));
    user.setLastName(params.get("userLastName"));
    user.setEmail(params.get("userEmail"));
    user.setPhone(params.get("userPhone"));
    user.setPassword(password);
    user.setRole(Role.VETERINARIAN);
    user.setActive(true);
    userService.saveComplete(user);

    VeterinaryClinic clinic = new VeterinaryClinic();
    clinic.setName(params.get("clinicName"));
    clinic.setAddress(params.get("clinicAddress"));
    clinic.setEmail(params.get("clinicEmail"));
    clinic.setPhone(params.get("clinicPhone"));
    clinic.setVeterinarian(user);
    clinic.setActive(true);
    save(clinic);
  }

  @Override
  @Transactional
  public void updateClinicData(User veterinarian, VeterinaryClinic updatedClinic) {
    VeterinaryClinic currentClinic = findByVeterinarianId(veterinarian.getId());
    updatedClinic.setId(currentClinic.getId());
    updatedClinic.setVeterinarian(veterinarian);
    update(updatedClinic);
  }

  private void updateClinicFields(VeterinaryClinic existing, VeterinaryClinic updated) {
    existing.setVeterinarian(updated.getVeterinarian());
    existing.setName(updated.getName());
    existing.setAddress(updated.getAddress());
    existing.setEmail(updated.getEmail());
    existing.setPhone(updated.getPhone());
  }
}
