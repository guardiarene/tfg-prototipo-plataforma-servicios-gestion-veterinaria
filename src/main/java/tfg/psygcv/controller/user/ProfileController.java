package tfg.psygcv.controller.user;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_LOGIN_LOGOUT;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_PROFILE;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.user.request.UpdateUserRequest;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.mapper.user.UserMapper;
import tfg.psygcv.service.clinic.VeterinaryClinicService;
import tfg.psygcv.service.user.UpdateUserProfileCommand;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/profile")
@Controller
public class ProfileController extends BaseController {

  private final UserService userService;
  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping
  public String showProfile(Model model, Authentication authentication) {
    User currentUser = getCurrentUser(authentication, userService);
    model.addAttribute("user", UserMapper.toResponse(userService.findById(currentUser.getId())));
    model.addAttribute("role", getAuthenticatedUser(authentication).getRole().name());
    return "profile/view";
  }

  @GetMapping("/edit")
  public String showEditForm(Model model, Authentication authentication) {
    User currentUser = getCurrentUser(authentication, userService);
    model.addAttribute(
        "user", UserMapper.toUpdateRequest(userService.findById(currentUser.getId())));
    model.addAttribute("role", getAuthenticatedUser(authentication).getRole().name());
    return "profile/edit";
  }

  @PostMapping("/edit")
  public String updateProfile(
      @Valid @ModelAttribute("user") UpdateUserRequest request,
      BindingResult result,
      Authentication authentication,
      Model model,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      model.addAttribute("role", getAuthenticatedUser(authentication).getRole().name());
      return "profile/edit";
    }
    try {
      User currentUser = getCurrentUser(authentication, userService);
      userService.updateVeterinarianProfile(
          currentUser.getId(),
          UpdateUserProfileCommand.builder()
              .firstName(request.getFirstName())
              .lastName(request.getLastName())
              .email(request.getEmail())
              .phone(request.getPhone())
              .build());
      ra.addFlashAttribute("success", "Perfil actualizado correctamente.");
      return REDIRECT_MY_PROFILE;
    } catch (Exception e) {
      model.addAttribute("role", getAuthenticatedUser(authentication).getRole().name());
      model.addAttribute("error", e.getMessage());
      return "profile/edit";
    }
  }

  @PostMapping("/deactivate")
  public String deactivateAccount(Authentication authentication, RedirectAttributes ra) {
    try {
      User currentUser = getCurrentUser(authentication, userService);
      if (currentUser.getRole() == Role.VETERINARIAN) {
        veterinaryClinicService.deactivate(
            veterinaryClinicService.findByVeterinarianId(currentUser.getId()).getId());
      }
      userService.deactivate(currentUser.getId());
      return REDIRECT_LOGIN_LOGOUT;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return REDIRECT_MY_PROFILE;
    }
  }
}
