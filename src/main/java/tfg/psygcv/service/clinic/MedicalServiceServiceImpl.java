package tfg.psygcv.service.clinic;

import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.entity.clinic.MedicalService;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.repository.clinic.MedicalServiceRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicalServiceServiceImpl implements MedicalServiceService {

  private final MedicalServiceRepository medicalServiceRepository;
  private final VeterinaryClinicService veterinaryClinicService;
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
  public List<MedicalService> findByVeterinarianClinic(Long veterinarianId) {
    medicalServiceValidator.validateId(veterinarianId);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarianId);
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
  public MedicalService save(CreateMedicalServiceCommand command) {
    medicalServiceValidator.validateForCreation(command);
    VeterinaryClinic clinic = veterinaryClinicService.findById(command.getClinicId());
    MedicalService service = new MedicalService();
    service.setName(command.getName());
    service.setDescription(command.getDescription());
    service.setClinic(clinic);
    return medicalServiceRepository.save(service);
  }

  @Override
  @Transactional
  public MedicalService update(Long serviceId, UpdateMedicalServiceCommand command, Long clinicId) {
    medicalServiceValidator.validateForUpdate(command);
    MedicalService existingService = findByIdAndValidateClinic(serviceId, clinicId);
    existingService.setName(command.getName());
    existingService.setDescription(command.getDescription());
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
}
