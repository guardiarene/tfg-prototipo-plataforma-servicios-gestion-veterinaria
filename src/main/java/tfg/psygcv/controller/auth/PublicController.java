package tfg.psygcv.controller.auth;

import static tfg.psygcv.config.constant.RouteConstant.VIEW_ACCESS_DENIED;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

  @GetMapping("/access_denied")
  public String accessDenied() {
    return VIEW_ACCESS_DENIED;
  }
}
