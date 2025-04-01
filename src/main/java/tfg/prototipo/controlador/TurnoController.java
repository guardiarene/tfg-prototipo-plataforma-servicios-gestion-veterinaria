package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.prototipo.modelo.*;
import tfg.prototipo.servicio.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/turnos")
public class TurnoController {

    private final ClinicaVeterinariaService clinicaVeterinariaService;

    private final ServicioMedicoService servicioMedicoService;

    private final MascotaService mascotaService;

    private final TurnoService turnoService;

    private final UsuarioService usuarioService;

    @GetMapping("/solicitar_turno/{id}")
    public String mostrarFormularioTurno(@PathVariable Long id, Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerPorId(id);
        List<ServicioMedico> servicios = servicioMedicoService.obtenerPorClinica(id);
        List<Mascota> mascotas = mascotaService.obtenerMascotasPorCliente(cliente.getId());

        model.addAttribute("clinicaVeterinaria", clinica);
        model.addAttribute("servicios", servicios);
        model.addAttribute("mascotas", mascotas);
        return "turnos/solicitar_turno";
    }

    @PostMapping("/solicitar_turno/{id}")
    public String solicitarTurno(@RequestParam("fecha") String fecha,
                                 @RequestParam("mascota") Long idMascota,
                                 @RequestParam("servicio") Long idServicioMedico,
                                 @RequestParam("id") Long idClinica,
                                 Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        turnoService.crearTurnoCliente(fecha, idMascota, idServicioMedico, idClinica, cliente);
        return "redirect:/turnos/mis_turnos";
    }

    @GetMapping("/mis_turnos")
    public String mostrarMisTurnos(Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        model.addAttribute("turnos", turnoService.obtenerTurnosPorCliente(cliente.getId()));
        return "turnos/mis_turnos";
    }

    @GetMapping("/detalle_turno/{id}")
    public String mostrarDetalleTurno(@PathVariable Long id, Model model, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Turno turno = turnoService.obtenerTurnoConDetalles(id);

        model.addAttribute("nombreVeterinario", turnoService.obtenerNombreVeterinario(turno));
        model.addAttribute("turno", turno);

        return usuario.getTipoRol() == Rol.RECEPCIONISTA
                ? "recepcionista/detalle_turno"
                : "turnos/detalle_turno";
    }

    @PostMapping("/actualizar_estado/{id}")
    public String actualizarEstadoTurno(@PathVariable Long id, @RequestParam("estado") EstadoTurno estado) {
        turnoService.actualizarEstadoTurno(id, estado);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @PostMapping("/cancelar_turno/{id}")
    public String cancelarTurno(@PathVariable Long id) {
        turnoService.cancelarTurno(id);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/reprogramar_turno/{id}")
    public String mostrarFormularioReprogramacion(@PathVariable Long id,
                                                  Model model,
                                                  Authentication authentication) {
        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());

        model.addAttribute("turno", turnoService.obtenerTurnoPorId(id));
        model.addAttribute("servicios", servicioMedicoService.obtenerPorClinica(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinario());

        return "recepcionista/reprogramar_turno";
    }

    @PostMapping("/reprogramar_turno/{id}")
    public String procesarReprogramacion(@PathVariable Long id,
                                         @ModelAttribute("turno") Turno turnoActualizado) {
        turnoService.reprogramarTurno(id, turnoActualizado);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/agendar_turno")
    public String mostrarFormularioNuevoTurno(@RequestParam(required = false) Long clienteId,
                                              Model model,
                                              Authentication authentication) {
        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());

        model.addAttribute("clienteId", clienteId);
        model.addAttribute("clientes", usuarioService.obtenerClientesActivos());
        model.addAttribute("mascotas", clienteId != null ? mascotaService.obtenerMascotasPorCliente(clienteId) : List.of());
        model.addAttribute("servicios", servicioMedicoService.obtenerPorClinica(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinario());
        model.addAttribute("turno", new Turno());

        return "recepcionista/agendar_turno";
    }

    @PostMapping("/agendar_turno")
    public String procesarNuevoTurno(@ModelAttribute("turno") Turno turno,
                                     @RequestParam("servicioId") Long servicioId,
                                     Authentication authentication) {
        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        turnoService.crearTurnoRecepcionista(turno, servicioId, recepcionista.getId());
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

}
