package tfg.psygcv.service.medical;

import java.util.List;
import tfg.psygcv.entity.medical.MedicalRecord;

public interface MedicalRecordService {

  MedicalRecord findCompleteById(Long medicalRecordId);

  List<MedicalRecord> findByVeterinarian(Long veterinarianId);

  MedicalRecord save(CreateMedicalRecordCommand command, Long veterinarianId);

  MedicalRecord update(Long id, UpdateMedicalRecordCommand command, Long veterinarianId);
}
