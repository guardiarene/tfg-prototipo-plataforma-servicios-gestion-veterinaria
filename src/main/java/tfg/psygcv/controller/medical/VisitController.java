package tfg.psygcv.controller.medical;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.medical.request.CreateVisitRequest;
import tfg.psygcv.dto.medical.request.UpdateVisitRequest;
import tfg.psygcv.dto.medical.response.MedicalRecordSummaryResponse;
import tfg.psygcv.dto.medical.response.VisitResponse;
import tfg.psygcv.entity.medical.Visit;
import tfg.psygcv.entity.medical.VisitType;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.mapper.medical.MedicalRecordMapper;
import tfg.psygcv.mapper.medical.VisitMapper;
import tfg.psygcv.service.medical.CreateVisitCommand;
import tfg.psygcv.service.medical.MedicalRecordService;
import tfg.psygcv.service.medical.UpdateVisitCommand;
import tfg.psygcv.service.medical.VisitService;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/visits")
@Controller
public class VisitController extends BaseController {

  private final VisitService visitService;
  private final MedicalRecordService medicalRecordService;
  private final UserService userService;

  @GetMapping("/{id}")
  public String showVisitDetails(@PathVariable Long id, Model model) {
    VisitResponse visit = VisitMapper.toResponse(visitService.findCompleteById(id));
    model.addAttribute("visit", visit);
    return "visits/details";
  }

  @GetMapping("/new")
  public String showNewVisitForm(@RequestParam Long medicalRecordId, Model model) {
    MedicalRecordSummaryResponse medicalRecord =
        MedicalRecordMapper.toSummary(medicalRecordService.findCompleteById(medicalRecordId));
    CreateVisitRequest visit = new CreateVisitRequest();
    visit.setDate(LocalDate.now());
    visit.setVisitType(VisitType.CONSULTATION);
    model.addAttribute("visit", visit);
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("medicalRecordId", medicalRecordId);
    model.addAttribute("visitTypes", VisitType.values());
    return "visits/new";
  }

  @PostMapping("/new")
  public String saveVisit(
      @RequestParam Long medicalRecordId,
      @Valid @ModelAttribute("visit") CreateVisitRequest request,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      MedicalRecordSummaryResponse medicalRecord =
          MedicalRecordMapper.toSummary(medicalRecordService.findCompleteById(medicalRecordId));
      model.addAttribute("medicalRecord", medicalRecord);
      model.addAttribute("medicalRecordId", medicalRecordId);
      model.addAttribute("visitTypes", VisitType.values());
      return "visits/new";
    }
    User veterinarian = getCurrentUser(authentication, userService);
    Visit savedVisit =
        visitService.createVisit(medicalRecordId, buildCreateCommand(request), veterinarian);
    return "redirect:/visits/" + savedVisit.getId();
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    Visit visit = visitService.findCompleteById(id);
    model.addAttribute("visit", VisitMapper.toUpdateRequest(visit));
    model.addAttribute("visitId", id);
    model.addAttribute("medicalRecordId", visit.getMedicalRecord().getId());
    model.addAttribute("visitTypes", VisitType.values());
    return "visits/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateVisit(
      @PathVariable Long id,
      @Valid @ModelAttribute("visit") UpdateVisitRequest request,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("visitId", id);
      model.addAttribute("visitTypes", VisitType.values());
      return "visits/edit";
    }
    User veterinarian = getCurrentUser(authentication, userService);
    Visit updatedVisit = visitService.updateVisit(id, buildUpdateCommand(request), veterinarian);
    return "redirect:/visits/" + updatedVisit.getId();
  }

  @PostMapping("/{id}/delete")
  public String deleteVisit(@PathVariable Long id, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    Visit visit = visitService.findCompleteById(id);
    Long medicalRecordId = visit.getMedicalRecord().getId();
    visitService.deleteVisit(id, veterinarian);
    return "redirect:/medical-records/" + medicalRecordId;
  }

  private CreateVisitCommand buildCreateCommand(CreateVisitRequest request) {
    return CreateVisitCommand.builder()
        .date(request.getDate())
        .reasonForVisit(request.getReasonForVisit())
        .visitType(request.getVisitType())
        .observations(request.getObservations())
        .clinicalExam(
            request.getClinicalExam() != null
                ? CreateVisitCommand.ClinicalExamData.builder()
                    .temperature(request.getClinicalExam().getTemperature())
                    .heartRate(request.getClinicalExam().getHeartRate())
                    .respiratoryRate(request.getClinicalExam().getRespiratoryRate())
                    .weight(request.getClinicalExam().getWeight())
                    .pulse(request.getClinicalExam().getPulse())
                    .mucosalMembranes(request.getClinicalExam().getMucosalMembranes())
                    .temperament(request.getClinicalExam().getTemperament())
                    .description(request.getClinicalExam().getDescription())
                    .build()
                : null)
        .anamnesis(
            request.getAnamnesis() != null
                ? CreateVisitCommand.AnamnesisData.builder()
                    .allergies(request.getAnamnesis().getAllergies())
                    .previousDiseases(request.getAnamnesis().getPreviousDiseases())
                    .surgeries(request.getAnamnesis().getSurgeries())
                    .currentMedications(request.getAnamnesis().getCurrentMedications())
                    .diet(request.getAnamnesis().getDiet())
                    .reproductiveStatus(request.getAnamnesis().getReproductiveStatus())
                    .lastDewormingDate(request.getAnamnesis().getLastDewormingDate())
                    .lastHeatDate(request.getAnamnesis().getLastHeatDate())
                    .lastBirthDate(request.getAnamnesis().getLastBirthDate())
                    .build()
                : null)
        .diagnostics(
            request.getDiagnostics() != null
                ? request.getDiagnostics().stream()
                    .map(
                        d ->
                            CreateVisitCommand.DiagnosticData.builder()
                                .problems(d.getProblems())
                                .build())
                    .toList()
                : List.of())
        .treatments(
            request.getTreatments() != null
                ? request.getTreatments().stream()
                    .map(
                        t ->
                            CreateVisitCommand.TreatmentData.builder()
                                .product(t.getProduct())
                                .route(t.getRoute())
                                .frequency(t.getFrequency())
                                .startDate(t.getStartDate())
                                .endDate(t.getEndDate())
                                .build())
                    .toList()
                : List.of())
        .vaccines(
            request.getVaccines() != null
                ? request.getVaccines().stream()
                    .map(
                        v ->
                            CreateVisitCommand.VaccineData.builder()
                                .applicationDate(v.getApplicationDate())
                                .brand(v.getBrand())
                                .dose(v.getDose())
                                .batch(v.getBatch())
                                .build())
                    .toList()
                : List.of())
        .build();
  }

  private UpdateVisitCommand buildUpdateCommand(UpdateVisitRequest request) {
    return UpdateVisitCommand.builder()
        .date(request.getDate())
        .reasonForVisit(request.getReasonForVisit())
        .visitType(request.getVisitType())
        .observations(request.getObservations())
        .clinicalExam(
            request.getClinicalExam() != null
                ? UpdateVisitCommand.ClinicalExamData.builder()
                    .temperature(request.getClinicalExam().getTemperature())
                    .heartRate(request.getClinicalExam().getHeartRate())
                    .respiratoryRate(request.getClinicalExam().getRespiratoryRate())
                    .weight(request.getClinicalExam().getWeight())
                    .pulse(request.getClinicalExam().getPulse())
                    .mucosalMembranes(request.getClinicalExam().getMucosalMembranes())
                    .temperament(request.getClinicalExam().getTemperament())
                    .description(request.getClinicalExam().getDescription())
                    .build()
                : null)
        .anamnesis(
            request.getAnamnesis() != null
                ? UpdateVisitCommand.AnamnesisData.builder()
                    .allergies(request.getAnamnesis().getAllergies())
                    .previousDiseases(request.getAnamnesis().getPreviousDiseases())
                    .surgeries(request.getAnamnesis().getSurgeries())
                    .currentMedications(request.getAnamnesis().getCurrentMedications())
                    .diet(request.getAnamnesis().getDiet())
                    .reproductiveStatus(request.getAnamnesis().getReproductiveStatus())
                    .lastDewormingDate(request.getAnamnesis().getLastDewormingDate())
                    .lastHeatDate(request.getAnamnesis().getLastHeatDate())
                    .lastBirthDate(request.getAnamnesis().getLastBirthDate())
                    .build()
                : null)
        .diagnostics(
            request.getDiagnostics() != null
                ? request.getDiagnostics().stream()
                    .map(
                        d ->
                            UpdateVisitCommand.DiagnosticData.builder()
                                .id(d.getId())
                                .problems(d.getProblems())
                                .build())
                    .toList()
                : List.of())
        .treatments(
            request.getTreatments() != null
                ? request.getTreatments().stream()
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
                    .toList()
                : List.of())
        .vaccines(
            request.getVaccines() != null
                ? request.getVaccines().stream()
                    .map(
                        v ->
                            UpdateVisitCommand.VaccineData.builder()
                                .id(v.getId())
                                .applicationDate(v.getApplicationDate())
                                .brand(v.getBrand())
                                .dose(v.getDose())
                                .batch(v.getBatch())
                                .build())
                    .toList()
                : List.of())
        .build();
  }
}
