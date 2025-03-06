package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.prototipo.servicio.UsuarioService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    private final UsuarioService usuarioService;

    @GetMapping("/interfaz_administrador")
    public String mostrarInterfazAdministrador(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodosUsuarios());
        return "administrador/interfaz_administrador";
    }

}
