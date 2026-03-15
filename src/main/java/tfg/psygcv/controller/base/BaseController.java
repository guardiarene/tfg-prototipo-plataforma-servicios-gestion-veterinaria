package tfg.psygcv.controller.base;

import org.springframework.security.core.Authentication;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.UserServiceInterface;

public abstract class BaseController {

  protected AuthenticatedUser getAuthenticatedUser(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      throw new IllegalStateException("Authenticated principal is not available");
    }
    if (principal instanceof AuthenticatedUser authenticatedUser) {
      return authenticatedUser;
    }
    throw new IllegalStateException(
        "Unsupported authenticated principal type: " + principal.getClass().getName());
  }

  protected User getCurrentUser(Authentication authentication, UserServiceInterface userService) {
    return userService.findById(getAuthenticatedUser(authentication).getId());
  }
}
