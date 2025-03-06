package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.UsuarioService;

import java.util.Arrays;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuario.setTipoRol(Rol.CLIENTE);
        usuarioService.registrar(usuario);
        return "redirect:/usuarios/login";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/login";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistroAdmin(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "administrador/nuevo_usuario";
    }

    @PostMapping("/nuevo")
    public String registrarUsuarioAdmin(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.registrarUsuarioCompleto(usuario);
        return "redirect:/administrador/interfaz_administrador";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        model.addAttribute("roles", Arrays.asList(Rol.values()));
        return "administrador/editar_usuario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuarioService.actualizarUsuarioCompleto(id, usuario);
        return "redirect:/administrador/interfaz_administrador";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.darseDeBaja(id);
        return "redirect:/administrador/interfaz_administrador";
    }

}
