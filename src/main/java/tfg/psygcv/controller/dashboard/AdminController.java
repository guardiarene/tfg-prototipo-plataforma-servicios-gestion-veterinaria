package tfg.psygcv.controller.dashboard;

import static tfg.psygcv.config.constant.RouteConstant.VIEW_ADMIN_DASHBOARD;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.user.response.UserSummaryResponse;
import tfg.psygcv.mapper.user.UserMapper;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

  private final UserService userService;

  @GetMapping("/dashboard")
  public String showAdminDashboard(Model model) {
    List<UserSummaryResponse> users =
        userService.findAll().stream().map(UserMapper::toSummary).toList();
    model.addAttribute("users", users);
    return VIEW_ADMIN_DASHBOARD;
  }
}
