package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.UsuarioService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    @GetMapping("/mi_perfil")
    public String mostrarPerfil(Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        model.addAttribute("usuario", usuarioService.obtenerPorId(cliente.getId()));
        return "perfil/mi_perfil";
    }

    @GetMapping("/mi_perfil/actualizar")
    public String mostrarFormularioActualizar(Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        model.addAttribute("usuario", usuarioService.obtenerPorId(cliente.getId()));
        return "perfil/actualizar_mi_perfil";
    }

    @PostMapping("/mi_perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuario, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        usuario.setId(cliente.getId());
        usuario.setTipoRol(Rol.CLIENTE);

        usuarioService.actualizar(usuario);
        return "redirect:/perfil/mi_perfil";
    }

    @PostMapping("/mi_perfil/darse_de_baja")
    public String darseDeBaja(Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        usuarioService.darseDeBaja(cliente.getId());
        return "redirect:/usuarios/login?logout";
    }

}
