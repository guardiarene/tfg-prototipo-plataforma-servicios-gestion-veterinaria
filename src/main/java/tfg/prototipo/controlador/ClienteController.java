package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.prototipo.servicio.ClinicaVeterinariaService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClinicaVeterinariaService clinicaVeterinariaService;

    @GetMapping("/interfaz_cliente")
    public String mostrarInterfazCliente(Model model) {
        model.addAttribute("clinicasVeterinarias", clinicaVeterinariaService.obtenerTodas());
        return "cliente/interfaz_cliente";
    }

}
