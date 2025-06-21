package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.service.impl.UserServiceImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/interfaz_administrador")
    public String mostrarInterfazAdministrador(Model model) {
        model.addAttribute("usuarios", userServiceImpl.findAll());
        return "administrador/interfaz_administrador";
    }

}
