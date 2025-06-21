package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.UserServiceImpl;

import java.util.Arrays;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new User());
        return "usuarios/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("user") User user) {
        user.setRole(Role.CUSTOMER);
        userServiceImpl.save(user);
        return "redirect:/usuarios/login";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("usuario", new User());
        return "usuarios/login";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistroAdmin(Model model) {
        model.addAttribute("usuario", new User());
        model.addAttribute("roles", Role.values());
        return "administrador/nuevo_usuario";
    }

    @PostMapping("/nuevo")
    public String registrarUsuarioAdmin(@ModelAttribute("user") User user) {
        userServiceImpl.saveComplete(user);
        return "redirect:/administrador/interfaz_administrador";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", userServiceImpl.findById(id));
        model.addAttribute("roles", Arrays.asList(Role.values()));
        return "administrador/editar_usuario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute User user) {
        userServiceImpl.updateComplete(id, user);
        return "redirect:/administrador/interfaz_administrador";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        userServiceImpl.deactivate(id);
        return "redirect:/administrador/interfaz_administrador";
    }

}
