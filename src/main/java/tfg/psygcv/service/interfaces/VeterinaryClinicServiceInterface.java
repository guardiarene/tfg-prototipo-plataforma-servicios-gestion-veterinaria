package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.User;

public interface VeterinaryClinicServiceInterface {

  List<VeterinaryClinic> findAll();

  VeterinaryClinic findById(Long clinicId);

  List<VeterinaryClinic> searchByName(String query);

  VeterinaryClinic findByOwnerId(Long ownerId);

  VeterinaryClinic findByVeterinarianId(Long veterinarianId);

  VeterinaryClinic findByReceptionistId(Long receptionistId);

  VeterinaryClinic save(VeterinaryClinic veterinaryClinic);

  VeterinaryClinic update(VeterinaryClinic veterinaryClinic);

  void deactivate(Long clinicId);

  void registerClinicWithVeterinarian(java.util.Map<String, String> params);

  void registerStaff(User owner, User staffUser);

  void updateClinicData(User veterinarian, VeterinaryClinic updatedClinic);
}
