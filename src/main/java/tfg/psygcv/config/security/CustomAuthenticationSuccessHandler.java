package tfg.psygcv.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Authentication authentication)
      throws IOException {
    String role =
        authentication.getAuthorities().stream()
            .findFirst()
            .map(authority -> authority.getAuthority())
            .orElseThrow(() -> new IllegalStateException("Authenticated user has no authorities"));
    String redirectUrl =
        switch (role) {
          case "ROLE_VETERINARIAN" -> "/veterinarian/dashboard";
          case "ROLE_RECEPTIONIST" -> "/receptionist/dashboard";
          case "ROLE_CUSTOMER" -> "/customer/dashboard";
          case "ROLE_SYSTEM_ADMINISTRATOR" -> "/admin/dashboard";
          default -> throw new IllegalStateException("Non-existent role: " + role);
        };
    response.sendRedirect(redirectUrl);
  }
}
