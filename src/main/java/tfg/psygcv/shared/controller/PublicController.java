package tfg.psygcv.shared.controller;

import static tfg.psygcv.shared.constant.RouteConstant.VIEW_ACCESS_DENIED;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublicController {

  @RequestMapping("/access_denied")
  public String accessDenied() {
    return VIEW_ACCESS_DENIED;
  }
}
