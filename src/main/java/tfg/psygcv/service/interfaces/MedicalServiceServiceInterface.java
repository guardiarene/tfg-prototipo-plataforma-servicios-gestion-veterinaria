package tfg.psygcv.service.interfaces;

import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.user.User;

import java.util.List;

public interface MedicalServiceServiceInterface {

    MedicalService findById(Long serviceId);

    List<MedicalService> findByClinicId(Long clinicId);

    List<MedicalService> findByVeterinarianClinic(User veterinarian);

    MedicalService findByIdAndValidateClinic(Long serviceId, Long clinicId);

    MedicalService save(MedicalService medicalService, Long clinicId);

    MedicalService update(Long serviceId, MedicalService updatedService, Long clinicId);

    void deactivate(Long serviceId, Long clinicId);

}
