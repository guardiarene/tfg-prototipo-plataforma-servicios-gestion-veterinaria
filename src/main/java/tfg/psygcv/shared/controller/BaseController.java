package tfg.psygcv.shared.controller;

import org.springframework.security.core.Authentication;
import tfg.psygcv.config.security.AuthenticatedUser;

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
}
