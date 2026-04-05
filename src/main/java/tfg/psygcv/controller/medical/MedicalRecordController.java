package tfg.psygcv.controller.medical;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_VETERINARIAN_DASHBOARD;

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
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.medical.request.CreateMedicalRecordRequest;
import tfg.psygcv.dto.medical.request.UpdateMedicalRecordRequest;
import tfg.psygcv.dto.medical.response.MedicalRecordResponse;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.mapper.medical.MedicalRecordMapper;
import tfg.psygcv.mapper.pet.PetMapper;
import tfg.psygcv.service.medical.CreateMedicalRecordCommand;
import tfg.psygcv.service.medical.MedicalRecordService;
import tfg.psygcv.service.medical.UpdateMedicalRecordCommand;
import tfg.psygcv.service.pet.PetService;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/medical-records")
@Controller
public class MedicalRecordController extends BaseController {

  private final MedicalRecordService medicalRecordService;
  private final PetService petService;
  private final UserService userService;

  @GetMapping("/{id}")
  public String showMedicalRecordDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    User user = getCurrentUser(authentication, userService);
    MedicalRecordResponse medicalRecord =
        MedicalRecordMapper.toResponse(medicalRecordService.findCompleteById(id));
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("role", user.getRole().name());
    return "medical_records/details";
  }

  @GetMapping("/new")
  public String showNewMedicalRecordForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    model.addAttribute("medicalRecord", new CreateMedicalRecordRequest());
    model.addAttribute(
        "pets",
        petService.findPetsWithAppointmentsInClinics(veterinarian.getId()).stream()
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
    User veterinarian = getCurrentUser(authentication, userService);
    if (result.hasErrors()) {
      model.addAttribute(
          "pets",
          petService.findPetsWithAppointmentsInClinics(veterinarian.getId()).stream()
              .map(PetMapper::toSummary)
              .toList());
      return "medical_records/new";
    }
    try {
      medicalRecordService.save(
          CreateMedicalRecordCommand.builder()
              .petId(request.getPetId())
              .generalObservations(request.getGeneralObservations())
              .build(),
          veterinarian.getId());
      ra.addFlashAttribute("success", "Historia clínica creada correctamente.");
      return REDIRECT_VETERINARIAN_DASHBOARD;
    } catch (Exception e) {
      model.addAttribute(
          "pets",
          petService.findPetsWithAppointmentsInClinics(veterinarian.getId()).stream()
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
      User veterinarian = getCurrentUser(authentication, userService);
      medicalRecordService.update(
          id,
          UpdateMedicalRecordCommand.builder()
              .generalObservations(request.getGeneralObservations())
              .build(),
          veterinarian.getId());
      ra.addFlashAttribute("success", "Historia clínica actualizada correctamente.");
      return "redirect:/medical-records/" + id;
    } catch (Exception e) {
      model.addAttribute("medicalRecordId", id);
      model.addAttribute("error", e.getMessage());
      return "medical_records/edit";
    }
  }
}
