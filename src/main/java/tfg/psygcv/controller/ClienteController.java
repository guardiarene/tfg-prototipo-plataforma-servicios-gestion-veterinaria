package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.service.impl.VeterinaryClinicServiceImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final VeterinaryClinicServiceImpl veterinaryClinicServiceImpl;

    @GetMapping("/interfaz_cliente")
    public String mostrarInterfazCliente(Model model) {
        model.addAttribute("clinicasVeterinarias", veterinaryClinicServiceImpl.findAll());
        return "cliente/interfaz_cliente";
    }

}
