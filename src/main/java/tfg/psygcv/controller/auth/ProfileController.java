package tfg.psygcv.controller.auth;

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
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.UserServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/profile")
@Controller
public class ProfileController extends BaseController {

  private final UserServiceInterface userService;

  @GetMapping
  public String showProfile(Model model, Authentication authentication) {
    User currentUser = getCurrentUser(authentication);
    model.addAttribute("user", userService.findById(currentUser.getId()));
    return "profile/view";
  }

  @GetMapping("/edit")
  public String showEditForm(Model model, Authentication authentication) {
    User currentUser = getCurrentUser(authentication);
    model.addAttribute("user", userService.findById(currentUser.getId()));
    return "profile/edit";
  }

  @PostMapping("/edit")
  public String updateProfile(
      @Valid @ModelAttribute("user") User user,
      BindingResult result,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "profile/edit";
    }
    User currentUser = getCurrentUser(authentication);
    user.setId(currentUser.getId());
    user.setRole(Role.CUSTOMER);
    userService.update(user);
    return REDIRECT_MY_PROFILE;
  }

  @PostMapping("/deactivate")
  public String deactivateAccount(Authentication authentication) {
    User currentUser = getCurrentUser(authentication);
    userService.deactivate(currentUser.getId());
    return REDIRECT_LOGIN_LOGOUT;
  }
}
