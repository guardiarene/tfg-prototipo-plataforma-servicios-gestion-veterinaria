package tfg.psygcv.controller.base;

import org.springframework.security.core.Authentication;
import tfg.psygcv.model.user.User;

public abstract class BaseController {

  protected User getCurrentUser(Authentication authentication) {
    return (User) authentication.getPrincipal();
  }
}
