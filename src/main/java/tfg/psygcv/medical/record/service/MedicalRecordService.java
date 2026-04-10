package tfg.psygcv.medical.record.service;

import java.util.List;
import tfg.psygcv.medical.record.command.CreateMedicalRecordCommand;
import tfg.psygcv.medical.record.command.UpdateMedicalRecordCommand;
import tfg.psygcv.medical.record.entity.MedicalRecord;

public interface MedicalRecordService {

  MedicalRecord findCompleteById(Long medicalRecordId);

  List<MedicalRecord> findByVeterinarian(Long veterinarianId);

  MedicalRecord save(CreateMedicalRecordCommand command, Long veterinarianId);

  MedicalRecord update(Long id, UpdateMedicalRecordCommand command, Long veterinarianId);
}
