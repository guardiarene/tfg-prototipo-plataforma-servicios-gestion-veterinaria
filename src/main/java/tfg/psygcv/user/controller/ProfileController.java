package tfg.psygcv.user.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_LOGIN_LOGOUT;
import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_PROFILE;

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
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.dto.request.UpdateUserRequest;
import tfg.psygcv.user.entity.Role;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.mapper.UserMapper;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/profile")
@Controller
public class ProfileController extends BaseController {

  private final UserService userService;
  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping
  public String showProfile(Model model, Authentication authentication) {
    var authUser = getAuthenticatedUser(authentication);
    User currentUser = userService.findById(authUser.getId());
    model.addAttribute("user", UserMapper.toResponse(currentUser));
    model.addAttribute("role", authUser.getRole().name());
    return "profile/view";
  }

  @GetMapping("/edit")
  public String showEditForm(Model model, Authentication authentication) {
    var authUser = getAuthenticatedUser(authentication);
    User currentUser = userService.findById(authUser.getId());
    model.addAttribute("user", UserMapper.toUpdateRequest(currentUser));
    model.addAttribute("role", authUser.getRole().name());
    return "profile/edit";
  }

  @PostMapping("/edit")
  public String updateProfile(
      @Valid @ModelAttribute("user") UpdateUserRequest request,
      BindingResult result,
      Authentication authentication,
      Model model,
      RedirectAttributes ra) {
    var authUser = getAuthenticatedUser(authentication);
    if (result.hasErrors()) {
      model.addAttribute("role", authUser.getRole().name());
      return "profile/edit";
    }
    try {
      userService.updateVeterinarianProfile(
          authUser.getId(), UserMapper.toUpdateProfileCommand(request));
      ra.addFlashAttribute("success", "Perfil actualizado correctamente.");
      return REDIRECT_MY_PROFILE;
    } catch (Exception e) {
      model.addAttribute("role", authUser.getRole().name());
      model.addAttribute("error", e.getMessage());
      return "profile/edit";
    }
  }

  @PostMapping("/deactivate")
  public String deactivateAccount(Authentication authentication, RedirectAttributes ra) {
    try {
      var authUser = getAuthenticatedUser(authentication);
      if (authUser.getRole() == Role.VETERINARIAN) {
        veterinaryClinicService.deactivate(
            veterinaryClinicService.findClinicIdByVeterinarianId(authUser.getId()));
      }
      userService.deactivate(authUser.getId());
      return REDIRECT_LOGIN_LOGOUT;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return REDIRECT_MY_PROFILE;
    }
  }
}
