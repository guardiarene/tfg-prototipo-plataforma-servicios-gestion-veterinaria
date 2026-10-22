package tfg.psygcv.clinic.service;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import tfg.psygcv.clinic.command.CreateMedicalServiceCommand;
import tfg.psygcv.clinic.command.UpdateMedicalServiceCommand;
import tfg.psygcv.clinic.entity.MedicalService;

public interface MedicalServiceService {

  MedicalService findById(Long serviceId);

  List<MedicalService> findByClinicId(Long clinicId);

  List<MedicalService> findByVeterinarianClinic(Long veterinarianId);

  MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId);

  MedicalService save(CreateMedicalServiceCommand command);

  @PreAuthorize("@authz.canAccessMedicalService(authentication, #serviceId)")
  MedicalService update(Long serviceId, UpdateMedicalServiceCommand command, Long clinicId);

  @PreAuthorize("@authz.canAccessMedicalService(authentication, #serviceId)")
  void deactivate(Long serviceId, Long clinicId);
}
