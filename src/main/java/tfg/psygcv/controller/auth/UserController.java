package tfg.psygcv.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.UserServiceInterface;

import static tfg.psygcv.config.constant.RouteConstant.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController extends BaseController {

    private final UserServiceInterface userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return VIEW_USER_REGISTER;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_USER_REGISTER;
        }
        user.setRole(Role.CUSTOMER);
        userService.save(user);
        return REDIRECT_LOGIN;
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
    public String registerUserByAdmin(@Valid @ModelAttribute("user") User user,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_ADMIN_NEW_USER;
        }
        userService.saveComplete(user);
        return REDIRECT_ADMIN_DASHBOARD;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", Role.values());
        return VIEW_ADMIN_EDIT_USER;
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_ADMIN_EDIT_USER;
        }
        userService.updateComplete(id, user);
        return REDIRECT_ADMIN_DASHBOARD;
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deactivate(id);
        return REDIRECT_ADMIN_DASHBOARD;
    }

}
