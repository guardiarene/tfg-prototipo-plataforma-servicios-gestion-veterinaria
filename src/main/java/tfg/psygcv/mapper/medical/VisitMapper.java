package tfg.psygcv.mapper.medical;

import java.util.ArrayList;
import tfg.psygcv.dto.medical.request.UpdateVisitRequest;
import tfg.psygcv.dto.medical.response.VisitResponse;
import tfg.psygcv.entity.medical.Visit;

public class VisitMapper {

  private VisitMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static VisitResponse toResponse(Visit visit) {
    VisitResponse response = new VisitResponse();
    response.setId(visit.getId());
    if (visit.getMedicalRecord() != null) {
      response.setMedicalRecordId(visit.getMedicalRecord().getId());
    }
    response.setDate(visit.getDate());
    response.setVisitType(visit.getVisitType());
    response.setReasonForVisit(visit.getReasonForVisit());
    response.setObservations(visit.getObservations());
    if (visit.getVeterinarian() != null) {
      response.setVeterinarianFullName(
          visit.getVeterinarian().getFirstName() + " " + visit.getVeterinarian().getLastName());
    }
    response.setClinicalExam(ClinicalExamMapper.toResponse(visit.getClinicalExam()));
    response.setAnamnesis(AnamnesisMapper.toResponse(visit.getAnamnesis()));
    response.setDiagnostics(
        visit.getDiagnostics().stream().map(DiagnosticMapper::toResponse).toList());
    response.setTreatments(
        visit.getTreatments().stream().map(TreatmentMapper::toResponse).toList());
    response.setVaccines(visit.getVaccines().stream().map(VaccineMapper::toResponse).toList());
    return response;
  }

  public static UpdateVisitRequest toUpdateRequest(Visit visit) {
    UpdateVisitRequest request = new UpdateVisitRequest();
    request.setDate(visit.getDate());
    request.setReasonForVisit(visit.getReasonForVisit());
    request.setVisitType(visit.getVisitType());
    request.setObservations(visit.getObservations());
    request.setClinicalExam(ClinicalExamMapper.toRequest(visit.getClinicalExam()));
    request.setAnamnesis(AnamnesisMapper.toRequest(visit.getAnamnesis()));
    request.setDiagnostics(
        visit.getDiagnostics() != null
            ? visit.getDiagnostics().stream().map(DiagnosticMapper::toRequest).toList()
            : new ArrayList<>());
    request.setTreatments(
        visit.getTreatments() != null
            ? visit.getTreatments().stream().map(TreatmentMapper::toRequest).toList()
            : new ArrayList<>());
    request.setVaccines(
        visit.getVaccines() != null
            ? visit.getVaccines().stream().map(VaccineMapper::toRequest).toList()
            : new ArrayList<>());
    return request;
  }
}
