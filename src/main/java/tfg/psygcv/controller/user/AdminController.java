package tfg.psygcv.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.service.interfaces.UserServiceInterface;

import static tfg.psygcv.config.constant.RouteConstant.VIEW_ADMIN_DASHBOARD;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

    private final UserServiceInterface userService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("users", userService.findAll());
        return VIEW_ADMIN_DASHBOARD;
    }

}
