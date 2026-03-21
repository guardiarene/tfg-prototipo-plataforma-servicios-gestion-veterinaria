package tfg.psygcv.controller.user;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_CLINIC_UPDATED;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  public String showDashboard(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    model.addAttribute("medicalRecords", medicalRecordService.findByVeterinarian(veterinarian));
    return "veterinarian/dashboard";
  }

  @GetMapping("/clinic")
  public String showClinicData(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("clinic", clinic);
    model.addAttribute("veterinarian", veterinarian);
    return "veterinarian/clinic";
  }

  @GetMapping("/clinic/edit")
  public String showEditClinicForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("veterinarian", veterinarian);
    model.addAttribute("clinic", clinic);
    return "veterinarian/edit_clinic";
  }

  @PostMapping("/profile/update")
  public String updateVeterinarianProfile(
      @ModelAttribute("veterinarian") User updatedVeterinarian,
      Model model,
      Authentication authentication) {
    User currentVeterinarian = getCurrentUser(authentication, userService);
    try {
      userService.updateVeterinarianProfile(currentVeterinarian, updatedVeterinarian);
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      model.addAttribute(
          "clinic", veterinaryClinicService.findByVeterinarianId(currentVeterinarian.getId()));
      model.addAttribute("errorMessage", "Error al actualizar el perfil profesional");
      return "veterinarian/edit_clinic";
    }
  }

  @PostMapping("/clinic/update")
  public String updateClinicData(
      @ModelAttribute("clinic") VeterinaryClinic updatedClinic,
      Model model,
      Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    try {
      veterinaryClinicService.updateClinicData(veterinarian, updatedClinic);
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      model.addAttribute("veterinarian", veterinarian);
      model.addAttribute("errorMessage", "Error al actualizar los datos de la clínica");
      return "veterinarian/edit_clinic";
    }
  }
}
