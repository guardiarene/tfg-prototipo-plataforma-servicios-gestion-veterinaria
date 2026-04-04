package tfg.psygcv.service.clinic;

import java.util.List;
import tfg.psygcv.entity.clinic.MedicalService;
import tfg.psygcv.entity.user.User;

public interface MedicalServiceService {

  MedicalService findById(Long serviceId);

  List<MedicalService> findByClinicId(Long clinicId);

  List<MedicalService> findByVeterinarianClinic(User veterinarian);

  MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId);

  MedicalService save(MedicalService medicalService, Long clinicId);

  MedicalService update(Long serviceId, MedicalService updatedService, Long clinicId);

  void deactivate(Long serviceId, Long clinicId);
}
