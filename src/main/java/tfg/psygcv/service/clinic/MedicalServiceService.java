package tfg.psygcv.service.clinic;

import java.util.List;
import tfg.psygcv.entity.clinic.MedicalService;

public interface MedicalServiceService {

  MedicalService findById(Long serviceId);

  List<MedicalService> findByClinicId(Long clinicId);

  List<MedicalService> findByVeterinarianClinic(Long veterinarianId);

  MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId);

  MedicalService save(CreateMedicalServiceCommand command);

  MedicalService update(Long serviceId, UpdateMedicalServiceCommand command, Long clinicId);

  void deactivate(Long serviceId, Long clinicId);
}
