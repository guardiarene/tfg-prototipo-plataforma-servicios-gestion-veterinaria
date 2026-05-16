package tfg.psygcv.clinic.service;

import java.util.List;
import tfg.psygcv.clinic.command.RegisterClinicWithVeterinarianCommand;
import tfg.psygcv.clinic.command.UpdateClinicCommand;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.user.command.CreateStaffCommand;

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

  Long findClinicIdByVeterinarianId(Long veterinarianId);

  Long findClinicIdByReceptionistId(Long receptionistId);

  void updateClinicData(Long veterinarianId, UpdateClinicCommand command);
}
