package tfg.psygcv.clinic.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.clinic.command.CreateMedicalServiceCommand;
import tfg.psygcv.clinic.command.UpdateMedicalServiceCommand;
import tfg.psygcv.clinic.entity.MedicalService;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.clinic.repository.MedicalServiceRepository;
import tfg.psygcv.exception.domain.ResourceNotFoundException;
import tfg.psygcv.exception.domain.UnauthorizedClinicAccessException;

@Slf4j
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
        .orElseThrow(() -> new ResourceNotFoundException("Medical service not found", serviceId));
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
    validateServiceBelongsToClinic(service, clinicId);
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
    MedicalService saved = medicalServiceRepository.save(service);
    log.info("Medical service created [serviceId={}, clinicId={}]",
        saved.getId(), command.getClinicId());
    return saved;
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
    log.info("Medical service deactivated [serviceId={}, clinicId={}]", serviceId, clinicId);
  }

  private void validateServiceBelongsToClinic(MedicalService service, Long clinicId) {
    if (!service.getClinic().getId().equals(clinicId)) {
      log.warn("Unauthorized clinic access attempt [serviceId={}, expectedClinicId={}]",
          service.getId(), clinicId);
      throw new UnauthorizedClinicAccessException(
          "Medical service does not belong to the specified clinic");
    }
  }
}
