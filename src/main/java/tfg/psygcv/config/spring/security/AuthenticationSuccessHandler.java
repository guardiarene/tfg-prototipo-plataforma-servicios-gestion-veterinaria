package tfg.psygcv.config.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.user.User;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String rol = user.getAuthorities().iterator().next().getAuthority();
        String redirectUrl = switch (rol) {
            case "ROLE_VETERINARIO" -> "/veterinario/interfaz_veterinario";
            case "ROLE_RECEPCIONISTA" -> "/recepcionista/interfaz_recepcionista";
            case "ROLE_CLIENTE" -> "/cliente/interfaz_cliente";
            case "ROLE_ADMINISTRADOR" -> "/administrador/interfaz_administrador";
            default -> throw new IllegalStateException("Role inexistente: " + rol);
        };
        response.sendRedirect(redirectUrl);
    }

}
