package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.EstadoTurno;
import tfg.prototipo.modelo.Turno;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.ClinicaVeterinariaService;
import tfg.prototipo.servicio.TurnoService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recepcionista")
public class RecepcionistaController {

    private final TurnoService turnoService;

    private final ClinicaVeterinariaService clinicaVeterinariaService;

    @GetMapping("/interfaz_recepcionista")
    public String mostrarInterfazRecepcionista(Model model, Authentication authentication) {
        Usuario recepcionista = (Usuario) authentication.getPrincipal();

        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());
        List<Turno> turnos = turnoService.obtenerPorClinica(clinica.getId());

        model.addAttribute("estadosTurno", EstadoTurno.values());
        model.addAttribute("turnos", turnos);
        return "recepcionista/interfaz_recepcionista";
    }

}
