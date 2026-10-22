package tfg.psygcv.clinic.service;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PreAuthorize("hasRole('VETERINARIAN')")
  void deactivate(Long clinicId);

  void registerClinicWithVeterinarian(RegisterClinicWithVeterinarianCommand command);

  @PreAuthorize("hasRole('VETERINARIAN')")
  void registerStaff(Long ownerId, CreateStaffCommand command);

  Long findClinicIdByVeterinarianId(Long veterinarianId);

  Long findClinicIdByReceptionistId(Long receptionistId);

  @PreAuthorize("hasRole('VETERINARIAN')")
  void updateClinicData(Long veterinarianId, UpdateClinicCommand command);
}
