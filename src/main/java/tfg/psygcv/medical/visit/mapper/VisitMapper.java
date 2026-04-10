package tfg.psygcv.medical.visit.mapper;

import java.util.ArrayList;
import java.util.List;
import tfg.psygcv.medical.visit.command.CreateVisitCommand;
import tfg.psygcv.medical.visit.command.UpdateVisitCommand;
import tfg.psygcv.medical.visit.dto.request.CreateVisitRequest;
import tfg.psygcv.medical.visit.dto.request.UpdateVisitRequest;
import tfg.psygcv.medical.visit.dto.response.VisitResponse;
import tfg.psygcv.medical.visit.entity.Visit;

public class VisitMapper {

  private VisitMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static CreateVisitCommand toCreateCommand(CreateVisitRequest request) {
    return CreateVisitCommand.builder()
        .date(request.getDate())
        .reasonForVisit(request.getReasonForVisit())
        .visitType(request.getVisitType())
        .observations(request.getObservations())
        .clinicalExam(mapCreateClinicalExam(request))
        .anamnesis(mapCreateAnamnesis(request))
        .diagnostics(mapCreateDiagnostics(request))
        .treatments(mapCreateTreatments(request))
        .vaccines(mapCreateVaccines(request))
        .build();
  }

  public static UpdateVisitCommand toUpdateCommand(UpdateVisitRequest request) {
    return UpdateVisitCommand.builder()
        .date(request.getDate())
        .reasonForVisit(request.getReasonForVisit())
        .visitType(request.getVisitType())
        .observations(request.getObservations())
        .clinicalExam(mapUpdateClinicalExam(request))
        .anamnesis(mapUpdateAnamnesis(request))
        .diagnostics(mapUpdateDiagnostics(request))
        .treatments(mapUpdateTreatments(request))
        .vaccines(mapUpdateVaccines(request))
        .build();
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

  private static CreateVisitCommand.ClinicalExamData mapCreateClinicalExam(
      CreateVisitRequest request) {
    if (request.getClinicalExam() == null) {
      return null;
    }
    return CreateVisitCommand.ClinicalExamData.builder()
        .temperature(request.getClinicalExam().getTemperature())
        .heartRate(request.getClinicalExam().getHeartRate())
        .respiratoryRate(request.getClinicalExam().getRespiratoryRate())
        .weight(request.getClinicalExam().getWeight())
        .pulse(request.getClinicalExam().getPulse())
        .mucosalMembranes(request.getClinicalExam().getMucosalMembranes())
        .temperament(request.getClinicalExam().getTemperament())
        .description(request.getClinicalExam().getDescription())
        .build();
  }

  private static CreateVisitCommand.AnamnesisData mapCreateAnamnesis(CreateVisitRequest request) {
    if (request.getAnamnesis() == null) {
      return null;
    }
    return CreateVisitCommand.AnamnesisData.builder()
        .allergies(request.getAnamnesis().getAllergies())
        .previousDiseases(request.getAnamnesis().getPreviousDiseases())
        .surgeries(request.getAnamnesis().getSurgeries())
        .currentMedications(request.getAnamnesis().getCurrentMedications())
        .diet(request.getAnamnesis().getDiet())
        .reproductiveStatus(request.getAnamnesis().getReproductiveStatus())
        .lastDewormingDate(request.getAnamnesis().getLastDewormingDate())
        .lastHeatDate(request.getAnamnesis().getLastHeatDate())
        .lastBirthDate(request.getAnamnesis().getLastBirthDate())
        .build();
  }

  private static List<CreateVisitCommand.DiagnosticData> mapCreateDiagnostics(
      CreateVisitRequest request) {
    if (request.getDiagnostics() == null) {
      return List.of();
    }
    return request.getDiagnostics().stream()
        .map(d -> CreateVisitCommand.DiagnosticData.builder().problems(d.getProblems()).build())
        .toList();
  }

  private static List<CreateVisitCommand.TreatmentData> mapCreateTreatments(
      CreateVisitRequest request) {
    if (request.getTreatments() == null) {
      return List.of();
    }
    return request.getTreatments().stream()
        .map(
            t ->
                CreateVisitCommand.TreatmentData.builder()
                    .product(t.getProduct())
                    .route(t.getRoute())
                    .frequency(t.getFrequency())
                    .startDate(t.getStartDate())
                    .endDate(t.getEndDate())
                    .build())
        .toList();
  }

  private static List<CreateVisitCommand.VaccineData> mapCreateVaccines(
      CreateVisitRequest request) {
    if (request.getVaccines() == null) {
      return List.of();
    }
    return request.getVaccines().stream()
        .map(
            v ->
                CreateVisitCommand.VaccineData.builder()
                    .applicationDate(v.getApplicationDate())
                    .brand(v.getBrand())
                    .dose(v.getDose())
                    .batch(v.getBatch())
                    .build())
        .toList();
  }

  private static UpdateVisitCommand.ClinicalExamData mapUpdateClinicalExam(
      UpdateVisitRequest request) {
    if (request.getClinicalExam() == null) {
      return null;
    }
    return UpdateVisitCommand.ClinicalExamData.builder()
        .temperature(request.getClinicalExam().getTemperature())
        .heartRate(request.getClinicalExam().getHeartRate())
        .respiratoryRate(request.getClinicalExam().getRespiratoryRate())
        .weight(request.getClinicalExam().getWeight())
        .pulse(request.getClinicalExam().getPulse())
        .mucosalMembranes(request.getClinicalExam().getMucosalMembranes())
        .temperament(request.getClinicalExam().getTemperament())
        .description(request.getClinicalExam().getDescription())
        .build();
  }

  private static UpdateVisitCommand.AnamnesisData mapUpdateAnamnesis(UpdateVisitRequest request) {
    if (request.getAnamnesis() == null) {
      return null;
    }
    return UpdateVisitCommand.AnamnesisData.builder()
        .allergies(request.getAnamnesis().getAllergies())
        .previousDiseases(request.getAnamnesis().getPreviousDiseases())
        .surgeries(request.getAnamnesis().getSurgeries())
        .currentMedications(request.getAnamnesis().getCurrentMedications())
        .diet(request.getAnamnesis().getDiet())
        .reproductiveStatus(request.getAnamnesis().getReproductiveStatus())
        .lastDewormingDate(request.getAnamnesis().getLastDewormingDate())
        .lastHeatDate(request.getAnamnesis().getLastHeatDate())
        .lastBirthDate(request.getAnamnesis().getLastBirthDate())
        .build();
  }

  private static List<UpdateVisitCommand.DiagnosticData> mapUpdateDiagnostics(
      UpdateVisitRequest request) {
    if (request.getDiagnostics() == null) {
      return List.of();
    }
    return request.getDiagnostics().stream()
        .map(
            d ->
                UpdateVisitCommand.DiagnosticData.builder()
                    .id(d.getId())
                    .problems(d.getProblems())
                    .build())
        .toList();
  }

  private static List<UpdateVisitCommand.TreatmentData> mapUpdateTreatments(
      UpdateVisitRequest request) {
    if (request.getTreatments() == null) {
      return List.of();
    }
    return request.getTreatments().stream()
        .map(
            t ->
                UpdateVisitCommand.TreatmentData.builder()
                    .id(t.getId())
                    .product(t.getProduct())
                    .route(t.getRoute())
                    .frequency(t.getFrequency())
                    .startDate(t.getStartDate())
                    .endDate(t.getEndDate())
                    .build())
        .toList();
  }

  private static List<UpdateVisitCommand.VaccineData> mapUpdateVaccines(
      UpdateVisitRequest request) {
    if (request.getVaccines() == null) {
      return List.of();
    }
    return request.getVaccines().stream()
        .map(
            v ->
                UpdateVisitCommand.VaccineData.builder()
                    .id(v.getId())
                    .applicationDate(v.getApplicationDate())
                    .brand(v.getBrand())
                    .dose(v.getDose())
                    .batch(v.getBatch())
                    .build())
        .toList();
  }
}
