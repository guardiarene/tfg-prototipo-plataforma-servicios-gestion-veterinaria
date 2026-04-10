package tfg.psygcv.medical.record.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_VETERINARIAN_DASHBOARD;

import jakarta.validation.Valid;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.medical.record.dto.request.CreateMedicalRecordRequest;
import tfg.psygcv.medical.record.dto.request.UpdateMedicalRecordRequest;
import tfg.psygcv.medical.record.dto.response.MedicalRecordResponse;
import tfg.psygcv.medical.record.mapper.MedicalRecordMapper;
import tfg.psygcv.medical.record.service.MedicalRecordService;
import tfg.psygcv.pet.mapper.PetMapper;
import tfg.psygcv.pet.service.PetService;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/medical-records")
@Controller
public class MedicalRecordController extends BaseController {

  private final MedicalRecordService medicalRecordService;
  private final PetService petService;

  @GetMapping("/{id}")
  public String showMedicalRecordDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    MedicalRecordResponse medicalRecord =
        MedicalRecordMapper.toResponse(medicalRecordService.findCompleteById(id));
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("role", getAuthenticatedUser(authentication).getRole().name());
    return "medical_records/details";
  }

  @GetMapping("/new")
  public String showNewMedicalRecordForm(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    model.addAttribute("medicalRecord", new CreateMedicalRecordRequest());
    model.addAttribute(
        "pets",
        petService.findPetsWithAppointmentsInClinics(veterinarianId).stream()
            .map(PetMapper::toSummary)
            .toList());
    return "medical_records/new";
  }

  @PostMapping("/new")
  public String saveMedicalRecord(
      @Valid @ModelAttribute("medicalRecord") CreateMedicalRecordRequest request,
      BindingResult result,
      Authentication authentication,
      Model model,
      RedirectAttributes ra) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    if (result.hasErrors()) {
      model.addAttribute(
          "pets",
          petService.findPetsWithAppointmentsInClinics(veterinarianId).stream()
              .map(PetMapper::toSummary)
              .toList());
      return "medical_records/new";
    }
    try {
      medicalRecordService.save(MedicalRecordMapper.toCreateCommand(request), veterinarianId);
      ra.addFlashAttribute("success", "Historia clínica creada correctamente.");
      return REDIRECT_VETERINARIAN_DASHBOARD;
    } catch (Exception e) {
      model.addAttribute(
          "pets",
          petService.findPetsWithAppointmentsInClinics(veterinarianId).stream()
              .map(PetMapper::toSummary)
              .toList());
      model.addAttribute("error", e.getMessage());
      return "medical_records/new";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    var entity = medicalRecordService.findCompleteById(id);
    var summary = MedicalRecordMapper.toSummary(entity);
    model.addAttribute("medicalRecord", MedicalRecordMapper.toUpdateRequest(entity));
    model.addAttribute("medicalRecordId", id);
    model.addAttribute(
        "petName",
        summary.getPetName() != null
            ? summary.getPetName()
                + (summary.getOwnerFullName() != null
                    ? " (" + summary.getOwnerFullName() + ")"
                    : "")
            : "");
    return "medical_records/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateMedicalRecord(
      @PathVariable Long id,
      @Valid @ModelAttribute("medicalRecord") UpdateMedicalRecordRequest request,
      BindingResult result,
      Authentication authentication,
      Model model,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      model.addAttribute("medicalRecordId", id);
      return "medical_records/edit";
    }
    try {
      Long veterinarianId = getAuthenticatedUser(authentication).getId();
      medicalRecordService.update(id, MedicalRecordMapper.toUpdateCommand(request), veterinarianId);
      ra.addFlashAttribute("success", "Historia clínica actualizada correctamente.");
      return "redirect:/medical-records/" + id;
    } catch (Exception e) {
      model.addAttribute("medicalRecordId", id);
      model.addAttribute("error", e.getMessage());
      return "medical_records/edit";
    }
  }
}
