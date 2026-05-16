package tfg.psygcv.user.controller;

import static tfg.psygcv.shared.constant.RouteConstant.VIEW_ADMIN_DASHBOARD;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.dto.response.UserSummaryResponse;
import tfg.psygcv.user.mapper.UserMapper;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminDashboardController extends BaseController {

  private final UserService userService;

  @GetMapping("/dashboard")
  public String showAdminDashboard(Model model) {
    List<UserSummaryResponse> users =
        userService.findAll().stream().map(UserMapper::toSummary).toList();
    model.addAttribute("users", users);
    return VIEW_ADMIN_DASHBOARD;
  }
}
