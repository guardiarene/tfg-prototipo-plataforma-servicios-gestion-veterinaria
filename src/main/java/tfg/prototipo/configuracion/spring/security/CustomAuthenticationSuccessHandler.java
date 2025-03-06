package tfg.prototipo.configuracion.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tfg.prototipo.modelo.Usuario;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String rol = usuario.getAuthorities().iterator().next().getAuthority();
        String redirectUrl = switch (rol) {
            case "ROLE_VETERINARIO" -> "/veterinario/interfaz_veterinario";
            case "ROLE_RECEPCIONISTA" -> "/recepcionista/interfaz_recepcionista";
            case "ROLE_CLIENTE" -> "/cliente/interfaz_cliente";
            case "ROLE_ADMINISTRADOR" -> "/administrador/interfaz_administrador";
            default -> throw new IllegalStateException("Rol inexistente: " + rol);
        };
        response.sendRedirect(redirectUrl);
    }

}
