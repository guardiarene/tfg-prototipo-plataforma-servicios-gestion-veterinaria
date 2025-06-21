package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.UserServiceImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/mi_perfil")
    public String mostrarPerfil(Model model, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        model.addAttribute("usuario", userServiceImpl.findById(cliente.getId()));
        return "perfil/mi_perfil";
    }

    @GetMapping("/mi_perfil/update")
    public String mostrarFormularioActualizar(Model model, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        model.addAttribute("usuario", userServiceImpl.findById(cliente.getId()));
        return "perfil/actualizar_mi_perfil";
    }

    @PostMapping("/mi_perfil/update")
    public String actualizarPerfil(@ModelAttribute("user") User user, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        user.setId(cliente.getId());
        user.setRole(Role.CUSTOMER);

        userServiceImpl.update(user);
        return "redirect:/perfil/mi_perfil";
    }

    @PostMapping("/mi_perfil/darse_de_baja")
    public String darseDeBaja(Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        userServiceImpl.deactivate(cliente.getId());
        return "redirect:/usuarios/login?logout";
    }

}
