package tfg.psygcv.controller.dashboard;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_CLINIC_UPDATED;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.clinic.request.UpdateClinicRequest;
import tfg.psygcv.dto.medical.response.MedicalRecordSummaryResponse;
import tfg.psygcv.dto.user.request.CreateStaffRequest;
import tfg.psygcv.dto.user.request.UpdateUserRequest;
import tfg.psygcv.dto.user.response.UserSummaryResponse;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.mapper.clinic.VeterinaryClinicMapper;
import tfg.psygcv.mapper.medical.MedicalRecordMapper;
import tfg.psygcv.mapper.user.UserMapper;
import tfg.psygcv.service.clinic.CreateStaffCommand;
import tfg.psygcv.service.clinic.UpdateClinicCommand;
import tfg.psygcv.service.clinic.VeterinaryClinicService;
import tfg.psygcv.service.medical.MedicalRecordService;
import tfg.psygcv.service.user.UpdateUserProfileCommand;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/veterinarian")
@Controller
public class VeterinarianController extends BaseController {

  private final MedicalRecordService medicalRecordService;
  private final VeterinaryClinicService veterinaryClinicService;
  private final UserService userService;

  @GetMapping("/dashboard")
  public String showDashboard(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    List<MedicalRecordSummaryResponse> medicalRecords =
        medicalRecordService.findByVeterinarian(veterinarian.getId()).stream()
            .map(MedicalRecordMapper::toSummary)
            .toList();
    model.addAttribute("medicalRecords", medicalRecords);
    return "veterinarian/dashboard";
  }

  @GetMapping("/clinic")
  public String showClinicData(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("clinic", VeterinaryClinicMapper.toResponse(clinic));
    model.addAttribute("veterinarian", UserMapper.toResponse(veterinarian));
    return "veterinarian/clinic";
  }

  @GetMapping("/clinic/edit")
  public String showEditClinicForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("veterinarian", UserMapper.toUpdateRequest(veterinarian));
    model.addAttribute("clinic", VeterinaryClinicMapper.toUpdateRequest(clinic));
    return "veterinarian/edit_clinic";
  }

  @GetMapping("/staff")
  public String listStaff(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    List<UserSummaryResponse> veterinarians =
        clinic.getVeterinarians().stream().map(UserMapper::toSummary).toList();
    List<UserSummaryResponse> receptionists =
        clinic.getReceptionists().stream().map(UserMapper::toSummary).toList();
    model.addAttribute("veterinarians", veterinarians);
    model.addAttribute("receptionists", receptionists);
    return "veterinarian/staff";
  }

  @GetMapping("/staff/new")
  public String showNewStaffForm(Model model) {
    model.addAttribute("staffUser", new CreateStaffRequest());
    return "veterinarian/new_staff";
  }

  @PostMapping("/staff/save")
  public String saveStaff(
      @Valid @ModelAttribute("staffUser") CreateStaffRequest request,
      BindingResult result,
      Model model,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "veterinarian/new_staff";
    }
    User owner = getCurrentUser(authentication, userService);
    try {
      veterinaryClinicService.registerStaff(
          owner.getId(),
          CreateStaffCommand.builder()
              .firstName(request.getFirstName())
              .lastName(request.getLastName())
              .email(request.getEmail())
              .password(request.getPassword())
              .phone(request.getPhone())
              .role(request.getRole())
              .build());
      return "redirect:/veterinarian/staff?created";
    } catch (Exception e) {
      model.addAttribute("error", "Error al registrar el personal: " + e.getMessage());
      return "veterinarian/new_staff";
    }
  }

  @PostMapping("/profile/update")
  public String updateVeterinarianProfile(
      @Valid @ModelAttribute("veterinarian") UpdateUserRequest request,
      BindingResult result,
      Model model,
      Authentication authentication) {
    User currentVeterinarian = getCurrentUser(authentication, userService);
    if (result.hasErrors()) {
      VeterinaryClinic clinic =
          veterinaryClinicService.findByVeterinarianId(currentVeterinarian.getId());
      model.addAttribute("clinic", VeterinaryClinicMapper.toUpdateRequest(clinic));
      return "veterinarian/edit_clinic";
    }
    try {
      userService.updateVeterinarianProfile(
          currentVeterinarian.getId(),
          UpdateUserProfileCommand.builder()
              .firstName(request.getFirstName())
              .lastName(request.getLastName())
              .email(request.getEmail())
              .phone(request.getPhone())
              .build());
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      VeterinaryClinic clinic =
          veterinaryClinicService.findByVeterinarianId(currentVeterinarian.getId());
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
    User owner = getCurrentUser(authentication, userService);
    if (result.hasErrors()) {
      model.addAttribute("veterinarian", UserMapper.toUpdateRequest(owner));
      return "veterinarian/edit_clinic";
    }
    try {
      veterinaryClinicService.updateClinicData(
          owner.getId(),
          UpdateClinicCommand.builder()
              .name(request.getName())
              .address(request.getAddress())
              .phone(request.getPhone())
              .email(request.getEmail())
              .build());
      return REDIRECT_MY_CLINIC_UPDATED;
    } catch (Exception e) {
      model.addAttribute("veterinarian", UserMapper.toUpdateRequest(owner));
      model.addAttribute(
          "errorClinic", "Error al actualizar los datos de la clínica: " + e.getMessage());
      return "veterinarian/edit_clinic";
    }
  }
}
