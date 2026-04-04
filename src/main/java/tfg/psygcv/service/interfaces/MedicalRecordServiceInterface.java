package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.entity.medical.MedicalRecord;
import tfg.psygcv.entity.user.User;

public interface MedicalRecordServiceInterface {

  MedicalRecord findCompleteById(Long clinicId);

  MedicalRecord findCompleteForEditing(Long id);

  List<MedicalRecord> findByVeterinarian(User veterinarian);

  MedicalRecord save(MedicalRecord medicalRecord, User veterinarian);

  MedicalRecord update(Long id, MedicalRecord updatedRecord, User veterinarian);
}
