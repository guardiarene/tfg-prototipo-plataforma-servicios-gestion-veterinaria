package tfg.psygcv.controller.user;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_ADMIN_DASHBOARD;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_LOGIN_REGISTERED;
import static tfg.psygcv.config.constant.RouteConstant.VIEW_ADMIN_EDIT_USER;
import static tfg.psygcv.config.constant.RouteConstant.VIEW_ADMIN_NEW_USER;
import static tfg.psygcv.config.constant.RouteConstant.VIEW_USER_LOGIN;
import static tfg.psygcv.config.constant.RouteConstant.VIEW_USER_REGISTER;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController extends BaseController {

  private final UserService userService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("registrationUser", new User());
    return VIEW_USER_REGISTER;
  }

  @PostMapping("/register")
  public String registerUser(
      @Valid @ModelAttribute("registrationUser") User user,
      BindingResult result,
      @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
      Model model) {
    if (result.hasErrors()) {
      return VIEW_USER_REGISTER;
    }
    if (confirmPassword == null || !confirmPassword.equals(user.getPassword())) {
      model.addAttribute("error", "Las contraseñas no coinciden.");
      return VIEW_USER_REGISTER;
    }
    try {
      user.setRole(Role.CUSTOMER);
      user.setActive(true);
      userService.save(user);
      return REDIRECT_LOGIN_REGISTERED;
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return VIEW_USER_REGISTER;
    }
  }

  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("user", new User());
    return VIEW_USER_LOGIN;
  }

  @GetMapping("/new")
  public String showAdminRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", Role.values());
    return VIEW_ADMIN_NEW_USER;
  }

  @PostMapping("/new")
  public String registerUserByAdmin(
      @Valid @ModelAttribute("user") User user,
      BindingResult result,
      Model model,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      model.addAttribute("roles", Role.values());
      return VIEW_ADMIN_NEW_USER;
    }
    try {
      userService.saveComplete(user);
      ra.addFlashAttribute("success", "Usuario registrado correctamente.");
      return REDIRECT_ADMIN_DASHBOARD;
    } catch (Exception e) {
      model.addAttribute("roles", Role.values());
      model.addAttribute("error", e.getMessage());
      return VIEW_ADMIN_NEW_USER;
    }
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("user", userService.findById(id));
    model.addAttribute("roles", Role.values());
    return VIEW_ADMIN_EDIT_USER;
  }

  @PostMapping("/edit/{id}")
  public String updateUser(
      @PathVariable Long id,
      @Valid @ModelAttribute User user,
      BindingResult result,
      Model model,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      model.addAttribute("roles", Role.values());
      return VIEW_ADMIN_EDIT_USER;
    }
    try {
      userService.updateComplete(id, user);
      ra.addFlashAttribute("success", "Usuario actualizado correctamente.");
      return REDIRECT_ADMIN_DASHBOARD;
    } catch (Exception e) {
      model.addAttribute("roles", Role.values());
      model.addAttribute("error", e.getMessage());
      return VIEW_ADMIN_EDIT_USER;
    }
  }

  @PostMapping("/delete/{id}")
  public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
    try {
      userService.deactivate(id);
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return REDIRECT_ADMIN_DASHBOARD;
  }
}
