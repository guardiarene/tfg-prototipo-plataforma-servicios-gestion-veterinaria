package tfg.psygcv.mapper.medical;

import tfg.psygcv.dto.medical.request.UpdateMedicalRecordRequest;
import tfg.psygcv.dto.medical.response.MedicalRecordResponse;
import tfg.psygcv.dto.medical.response.MedicalRecordSummaryResponse;
import tfg.psygcv.entity.medical.MedicalRecord;

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
        summary.setOwnerFullName(
            medicalRecord.getPet().getOwner().getFirstName()
                + " "
                + medicalRecord.getPet().getOwner().getLastName());
      }
    }
    return summary;
  }

  public static MedicalRecordResponse toResponse(MedicalRecord medicalRecord) {
    MedicalRecordResponse response = new MedicalRecordResponse();
    response.setId(medicalRecord.getId());
    response.setGeneralObservations(medicalRecord.getGeneralObservations());
    if (medicalRecord.getPet() != null) {
      response.setPetName(medicalRecord.getPet().getName());
      response.setPetSpeciesDescription(medicalRecord.getPet().getSpecies().getDescription());
      response.setPetBreedDescription(medicalRecord.getPet().getBreed().getDescription());
      if (medicalRecord.getPet().getOwner() != null) {
        response.setOwnerFullName(
            medicalRecord.getPet().getOwner().getFirstName()
                + " "
                + medicalRecord.getPet().getOwner().getLastName());
      }
    }
    response.setCurrentAnamnesis(AnamnesisMapper.toResponse(medicalRecord.getCurrentAnamnesis()));
    response.setVaccines(
        medicalRecord.getVaccines().stream().map(VaccineMapper::toResponse).toList());
    response.setVisits(medicalRecord.getVisits().stream().map(VisitMapper::toResponse).toList());
    return response;
  }

  public static UpdateMedicalRecordRequest toUpdateRequest(MedicalRecord medicalRecord) {
    UpdateMedicalRecordRequest request = new UpdateMedicalRecordRequest();
    request.setGeneralObservations(medicalRecord.getGeneralObservations());
    return request;
  }
}
