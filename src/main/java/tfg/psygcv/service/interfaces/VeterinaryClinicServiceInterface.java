package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.model.clinic.VeterinaryClinic;

public interface VeterinaryClinicServiceInterface {

  List<VeterinaryClinic> findAll();

  VeterinaryClinic findById(Long clinicId);

  List<VeterinaryClinic> searchByName(String query);

  VeterinaryClinic findByVeterinarianId(Long veterinarianId);

  VeterinaryClinic findByReceptionistId(Long receptionistId);

  VeterinaryClinic save(VeterinaryClinic veterinaryClinic);

  VeterinaryClinic update(VeterinaryClinic veterinaryClinic);

  void deactivate(Long clinicId);
}
