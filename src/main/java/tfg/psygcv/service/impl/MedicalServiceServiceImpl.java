package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.MedicalServiceRepository;
import tfg.psygcv.service.interfaces.MedicalServiceServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;
import tfg.psygcv.service.validator.MedicalServiceValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicalServiceServiceImpl implements MedicalServiceServiceInterface {

  private final MedicalServiceRepository medicalServiceRepository;

  private final VeterinaryClinicServiceInterface veterinaryClinicService;

  private final MedicalServiceValidator medicalServiceValidator;

  @Override
  public MedicalService findById(Long serviceId) {
    medicalServiceValidator.validateId(serviceId);
    return medicalServiceRepository
        .findByIdAndActive(serviceId)
        .orElseThrow(
            () -> new EntityNotFoundException("Medical service not found with ID: " + serviceId));
  }

  @Override
  public List<MedicalService> findByClinicId(Long clinicId) {
    medicalServiceValidator.validateId(clinicId);
    return medicalServiceRepository.findByClinicId(clinicId);
  }

  @Override
  public List<MedicalService> findByVeterinarianClinic(User veterinarian) {
    medicalServiceValidator.validateVeterinarian(veterinarian);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    return medicalServiceRepository.findByClinicId(clinic.getId());
  }

  @Override
  public MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId) {
    MedicalService service = findById(serviceId);
    try {
      validateServiceBelongsToClinic(service, clinicId);
    } catch (AccessDeniedException ex) {
      throw new RuntimeException(ex.getMessage());
    }
    return service;
  }

  @Override
  @Transactional
  public MedicalService save(MedicalService medicalService, Long clinicId) {
    medicalServiceValidator.validateForCreation(medicalService);
    medicalServiceValidator.validateId(clinicId);
    VeterinaryClinic clinic = veterinaryClinicService.findById(clinicId);
    medicalService.setClinic(clinic);
    return medicalServiceRepository.save(medicalService);
  }

  @Override
  @Transactional
  public MedicalService update(Long serviceId, MedicalService updatedService, Long clinicId) {
    medicalServiceValidator.validateForUpdate(updatedService);
    MedicalService existingService = findByIdAndValidateClinic(serviceId, clinicId);
    updateServiceFields(existingService, updatedService);
    return medicalServiceRepository.save(existingService);
  }

  @Override
  @Transactional
  public void deactivate(Long serviceId, Long clinicId) {
    MedicalService service = findByIdAndValidateClinic(serviceId, clinicId);
    service.setActive(false);
    medicalServiceRepository.save(service);
  }

  private void validateServiceBelongsToClinic(MedicalService service, Long clinicId)
      throws AccessDeniedException {
    if (!service.getClinic().getId().equals(clinicId)) {
      throw new AccessDeniedException("Medical service does not belong to the specified clinic");
    }
  }

  private void updateServiceFields(MedicalService existing, MedicalService updated) {
    existing.setName(updated.getName());
    existing.setDescription(updated.getDescription());
  }
}
