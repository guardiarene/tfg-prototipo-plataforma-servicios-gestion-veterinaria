package tfg.psygcv.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.user.User;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String role = user.getAuthorities().iterator().next().getAuthority();
        String redirectUrl = switch (role) {
            case "ROLE_VETERINARIO" -> "/veterinarian/dashboard";
            case "ROLE_RECEPCIONISTA" -> "/receptionist/dashboard";
            case "ROLE_CLIENTE" -> "/client/dashboard";
            case "ROLE_ADMINISTRADOR" -> "/admin/dashboard";
            default -> throw new IllegalStateException("Non-existent role: " + role);
        };
        response.sendRedirect(redirectUrl);
    }

}
