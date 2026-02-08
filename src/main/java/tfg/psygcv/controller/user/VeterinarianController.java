package tfg.psygcv.controller.user;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_CLINIC;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/veterinarian")
@Controller
public class VeterinarianController extends BaseController {

  private final MedicalRecordServiceInterface medicalRecordService;

  private final VeterinaryClinicServiceInterface veterinaryClinicService;

  private final UserServiceInterface userService;

  @GetMapping("/dashboard")
  public String showDashboard(Model model, @AuthenticationPrincipal User veterinarian) {
    model.addAttribute("medicalRecords", medicalRecordService.findByVeterinarian(veterinarian));
    return "veterinarian/dashboard";
  }

  @GetMapping("/clinic")
  public String showClinicData(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());

    model.addAttribute("clinic", clinic);
    model.addAttribute("veterinarian", veterinarian);
    return "veterinarian/clinic";
  }

  @GetMapping("/clinic/edit")
  public String showEditClinicForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());

    model.addAttribute("veterinarian", veterinarian);
    model.addAttribute("clinic", clinic);
    return "veterinarian/edit_clinic";
  }

  @PostMapping("/profile/update")
  public String updateVeterinarianProfile(
      @Valid @ModelAttribute("veterinarian") User updatedVeterinarian,
      BindingResult result,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "veterinarian/edit_clinic";
    }
    User currentVeterinarian = getCurrentUser(authentication);
    updatedVeterinarian.setId(currentVeterinarian.getId());
    userService.update(updatedVeterinarian);
    return REDIRECT_MY_CLINIC;
  }

  @PostMapping("/clinic/update")
  public String updateClinicData(
      @Valid @ModelAttribute("clinic") VeterinaryClinic updatedClinic,
      BindingResult result,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "veterinarian/edit_clinic";
    }
    User veterinarian = getCurrentUser(authentication);
    VeterinaryClinic currentClinic =
        veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    updatedClinic.setId(currentClinic.getId());
    veterinaryClinicService.update(updatedClinic);
    return REDIRECT_MY_CLINIC;
  }
}
