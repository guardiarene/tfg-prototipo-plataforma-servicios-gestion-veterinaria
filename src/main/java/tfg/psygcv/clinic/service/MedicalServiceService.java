package tfg.psygcv.clinic.service;

import java.util.List;
import tfg.psygcv.clinic.command.CreateMedicalServiceCommand;
import tfg.psygcv.clinic.command.UpdateMedicalServiceCommand;
import tfg.psygcv.clinic.entity.MedicalService;

public interface MedicalServiceService {

  MedicalService findById(Long serviceId);

  List<MedicalService> findByClinicId(Long clinicId);

  List<MedicalService> findByVeterinarianClinic(Long veterinarianId);

  MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId);

  MedicalService save(CreateMedicalServiceCommand command);

  MedicalService update(Long serviceId, UpdateMedicalServiceCommand command, Long clinicId);

  void deactivate(Long serviceId, Long clinicId);
}
