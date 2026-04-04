package tfg.psygcv.service.clinic;

import java.util.List;
import tfg.psygcv.entity.clinic.VeterinaryClinic;

public interface VeterinaryClinicService {

  List<VeterinaryClinic> findAll();

  VeterinaryClinic findById(Long clinicId);

  List<VeterinaryClinic> searchByName(String query);

  VeterinaryClinic findByOwnerId(Long ownerId);

  VeterinaryClinic findByVeterinarianId(Long veterinarianId);

  VeterinaryClinic findByReceptionistId(Long receptionistId);

  void deactivate(Long clinicId);

  void registerClinicWithVeterinarian(RegisterClinicWithVeterinarianCommand command);

  void registerStaff(Long ownerId, CreateStaffCommand command);

  void updateClinicData(Long veterinarianId, UpdateClinicCommand command);
}
