package tfg.psygcv.service.interfaces;

import tfg.psygcv.model.clinic.VeterinaryClinic;

import java.util.List;

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
