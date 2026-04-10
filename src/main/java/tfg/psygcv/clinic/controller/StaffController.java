package tfg.psygcv.clinic.controller;

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
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.dto.request.CreateStaffRequest;
import tfg.psygcv.user.dto.response.UserSummaryResponse;
import tfg.psygcv.user.mapper.UserMapper;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/veterinarian/staff")
@Controller
public class StaffController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;
  private final UserService userService;

  @GetMapping
  public String listStaff(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    Long clinicId = veterinaryClinicService.findClinicIdByVeterinarianId(veterinarianId);
    List<UserSummaryResponse> veterinarians =
        userService.findVeterinariansByClinicId(clinicId).stream()
            .map(UserMapper::toSummary)
            .toList();
    List<UserSummaryResponse> receptionists =
        userService.findReceptionistsByClinicId(clinicId).stream()
            .map(UserMapper::toSummary)
            .toList();
    model.addAttribute("veterinarians", veterinarians);
    model.addAttribute("receptionists", receptionists);
    return "veterinarian/staff";
  }

  @GetMapping("/new")
  public String showNewStaffForm(Model model) {
    model.addAttribute("staffUser", new CreateStaffRequest());
    return "veterinarian/new_staff";
  }

  @PostMapping("/save")
  public String saveStaff(
      @Valid @ModelAttribute("staffUser") CreateStaffRequest request,
      BindingResult result,
      Model model,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "veterinarian/new_staff";
    }
    Long ownerId = getAuthenticatedUser(authentication).getId();
    try {
      veterinaryClinicService.registerStaff(ownerId, UserMapper.toCreateStaffCommand(request));
      return "redirect:/veterinarian/staff?created";
    } catch (Exception e) {
      model.addAttribute("error", "Error al registrar el personal: " + e.getMessage());
      return "veterinarian/new_staff";
    }
  }
}
