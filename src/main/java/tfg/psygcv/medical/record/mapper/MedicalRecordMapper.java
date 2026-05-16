package tfg.psygcv.medical.record.mapper;

import tfg.psygcv.medical.record.command.CreateMedicalRecordCommand;
import tfg.psygcv.medical.record.command.UpdateMedicalRecordCommand;
import tfg.psygcv.medical.record.dto.request.CreateMedicalRecordRequest;
import tfg.psygcv.medical.record.dto.request.UpdateMedicalRecordRequest;
import tfg.psygcv.medical.record.dto.response.MedicalRecordResponse;
import tfg.psygcv.medical.record.dto.response.MedicalRecordSummaryResponse;
import tfg.psygcv.medical.record.entity.MedicalRecord;
import tfg.psygcv.medical.visit.entity.Anamnesis;
import tfg.psygcv.medical.visit.mapper.AnamnesisMapper;
import tfg.psygcv.medical.visit.mapper.VaccineMapper;
import tfg.psygcv.medical.visit.mapper.VisitMapper;

public class MedicalRecordMapper {

  private MedicalRecordMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static MedicalRecordSummaryResponse toSummary(MedicalRecord medicalRecord) {
    MedicalRecordSummaryResponse summary = new MedicalRecordSummaryResponse();
    summary.setId(medicalRecord.getId());
    if (medicalRecord.getPet() != null) {
      summary.setPetName(medicalRecord.getPet().getName());
      summary.setPetSpeciesDescription(medicalRecord.getPet().getSpecies().getDescription());
      if (medicalRecord.getPet().getOwner() != null) {
        summary.setOwnerFullName(medicalRecord.getPet().getOwner().getFirstName() + " "
            + medicalRecord.getPet().getOwner().getLastName());
      }
    }
    return summary;
  }

  public static MedicalRecordResponse toResponse(MedicalRecord medicalRecord,
                                                 Anamnesis currentAnamnesis) {
    MedicalRecordResponse response = new MedicalRecordResponse();
    response.setId(medicalRecord.getId());
    response.setGeneralObservations(medicalRecord.getGeneralObservations());
    if (medicalRecord.getPet() != null) {
      response.setPetName(medicalRecord.getPet().getName());
      response.setPetSpeciesDescription(medicalRecord.getPet().getSpecies().getDescription());
      response.setPetBreedDescription(medicalRecord.getPet().getBreed().getDescription());
      if (medicalRecord.getPet().getOwner() != null) {
        response.setOwnerFullName(medicalRecord.getPet().getOwner().getFirstName() + " "
            + medicalRecord.getPet().getOwner().getLastName());
      }
    }
    response.setCurrentAnamnesis(AnamnesisMapper.toResponse(currentAnamnesis));
    response.setVaccines(
        medicalRecord.getVaccines().stream().map(VaccineMapper::toResponse).toList());
    response.setVisits(medicalRecord.getVisits().stream().map(VisitMapper::toResponse).toList());
    return response;
  }

  public static CreateMedicalRecordCommand toCreateCommand(CreateMedicalRecordRequest request) {
    return CreateMedicalRecordCommand.builder().petId(request.getPetId())
        .generalObservations(request.getGeneralObservations()).build();
  }

  public static UpdateMedicalRecordCommand toUpdateCommand(UpdateMedicalRecordRequest request) {
    return UpdateMedicalRecordCommand.builder()
        .generalObservations(request.getGeneralObservations()).build();
  }

  public static UpdateMedicalRecordRequest toUpdateRequest(MedicalRecord medicalRecord) {
    UpdateMedicalRecordRequest request = new UpdateMedicalRecordRequest();
    request.setGeneralObservations(medicalRecord.getGeneralObservations());
    return request;
  }
}
