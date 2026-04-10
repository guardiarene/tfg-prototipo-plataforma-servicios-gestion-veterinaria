package tfg.psygcv.clinic.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_CLINIC_UPDATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.clinic.dto.request.UpdateClinicRequest;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.clinic.mapper.VeterinaryClinicMapper;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.dto.request.UpdateUserRequest;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.mapper.UserMapper;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/veterinarian")
@Controller
public class ClinicManagementController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;
  private final UserService userService;

  @GetMapping("/clinic")
  public String showClinicData(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    User veterinarian = userService.findById(veterinarianId);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarianId);
    model.addAttribute("clinic", VeterinaryClinicMapper.toResponse(clinic));
    model.addAttribute("veterinarian", UserMapper.toResponse(veterinarian));
    return "veterinarian/clinic";
  }

  @GetMapping("/clinic/edit")
  public String showEditClinicForm(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    User veterinarian = userService.findById(veterinarianId);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarianId);
    model.addAttribute("veterinarian", UserMapper.toUpdateRequest(veterinarian));
    model.addAttribute("clinic", VeterinaryClinicMapper.toUpdateRequest(clinic));
    return "veterinarian/edit_clinic";
  }

  @PostMapping("/profile/update")
  public String updateVeterinarianProfile(
      @Valid @ModelAttribute("veterinarian") UpdateUserRequest request,
      BindingResult result,
      Model model,
      Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    if (result.hasErrors()) {
      VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarianId);
      model.addAttribute("clinic", VeterinaryClinicMapper.toUpdateRequest(clinic));
      return "veterinarian/edit_clinic";
    }
    try {
      userService.updateVeterinarianProfile(
          veterinarianId, UserMapper.toUpdateProfileCommand(request));
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarianId);
      model.addAttribute("clinic", VeterinaryClinicMapper.toUpdateRequest(clinic));
      model.addAttribute(
          "errorProfile", "Error al actualizar el perfil profesional: " + e.getMessage());
      return "veterinarian/edit_clinic";
    }
  }

  @PostMapping("/clinic/update")
  public String updateClinicData(
      @Valid @ModelAttribute("clinic") UpdateClinicRequest request,
      BindingResult result,
      Model model,
      Authentication authentication) {
    Long ownerId = getAuthenticatedUser(authentication).getId();
    User owner = userService.findById(ownerId);
    if (result.hasErrors()) {
      model.addAttribute("veterinarian", UserMapper.toUpdateRequest(owner));
      return "veterinarian/edit_clinic";
    }
    try {
      veterinaryClinicService.updateClinicData(
          ownerId, VeterinaryClinicMapper.toUpdateClinicCommand(request));
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      model.addAttribute("veterinarian", UserMapper.toUpdateRequest(owner));
      model.addAttribute(
          "errorClinic", "Error al actualizar los datos de la clínica: " + e.getMessage());
      return "veterinarian/edit_clinic";
    }
  }
}
