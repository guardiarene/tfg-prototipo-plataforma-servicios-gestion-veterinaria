package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.prototipo.modelo.*;
import tfg.prototipo.servicio.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerPorId(id).orElseThrow();
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
        Mascota mascota = mascotaService.obtenerMascota(idMascota);
        ServicioMedico servicio = servicioMedicoService.obtenerPorId(idServicioMedico);
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerPorId(idClinica).orElseThrow();

        Turno turno = new Turno();
        turno.setFecha(LocalDate.parse(fecha));
        turno.setHora(LocalTime.of(0, 0));
        turno.setClinica(clinica);
        turno.setServicioMedico(servicio);
        turno.setCliente(cliente);
        turno.setEstado(EstadoTurno.PENDIENTE);
        turno.setMascota(mascota);

        turnoService.crearTurno(turno);
        return "redirect:/turnos/mis_turnos";
    }

    @GetMapping("/mis_turnos")
    public String mostrarMisTurnos(Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        List<Turno> turnos = turnoService.obtenerPorCliente(cliente.getId());

        model.addAttribute("turnos", turnos);
        return "turnos/mis_turnos";
    }

    @GetMapping("/detalle_turno/{id}")
    public String mostrarDetalleTurno(@PathVariable Long id, Model model, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Turno turno = turnoService.obtenerPorId(id).orElseThrow();
        String nombreVeterinario = "Veterinario no asignado";

        if (turno.getServicioMedico() != null && turno.getServicioMedico().getClinica().getVeterinario().getNombre() != null) {
            nombreVeterinario = turno.getServicioMedico().getClinica().getVeterinario().getNombre();
        }

        model.addAttribute("nombreVeterinario", nombreVeterinario);
        model.addAttribute("turno", turno);

        return usuario.getTipoRol() == Rol.RECEPCIONISTA
                ? "recepcionista/detalle_turno"
                : "turnos/detalle_turno";
    }

    @PostMapping("/actualizar_estado/{id}")
    public String actualizarEstadoTurno(@PathVariable Long id, @RequestParam("estado") EstadoTurno estado) {
        turnoService.actualizarEstado(id, estado);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @PostMapping("/cancelar_turno/{id}")
    public String cancelarTurno(@PathVariable Long id) {
        turnoService.actualizarEstado(id, EstadoTurno.CANCELADO);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/reprogramar_turno/{id}")
    public String mostrarFormularioReprogramacion(@PathVariable Long id,
                                                  Model model,
                                                  Authentication authentication) {
        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        Turno turno = turnoService.obtenerPorId(id).orElseThrow();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());

        model.addAttribute("turno", turno);
        model.addAttribute("servicios", servicioMedicoService.obtenerPorClinica(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinario());

        return "recepcionista/reprogramar_turno";
    }

    @PostMapping("/reprogramar_turno/{id}")
    public String procesarReprogramacion(@PathVariable Long id,
                                         @ModelAttribute("turno") Turno turnoActualizado) {
        Turno turno = turnoService.obtenerPorId(id).orElseThrow();
        turno.setFecha(turnoActualizado.getFecha());
        turno.setHora(turnoActualizado.getHora());
        turno.setServicioMedico(turnoActualizado.getServicioMedico());

        turnoService.actualizarTurno(turno);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/agendar_turno")
    public String mostrarFormularioNuevoTurno(@RequestParam(required = false) Long clienteId,
                                              Model model,
                                              Authentication authentication) {

        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());

        Turno turno = new Turno();
        List<Mascota> mascotas = new ArrayList<>();

        if (clienteId != null) {
            Usuario cliente = usuarioService.obtenerPorId(clienteId);
            turno.setCliente(cliente);
            mascotas = mascotaService.obtenerMascotasPorCliente(clienteId);
        }

        model.addAttribute("clienteId", clienteId);
        model.addAttribute("clientes", usuarioService.obtenerClientesActivos());
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("servicios", servicioMedicoService.obtenerPorClinica(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinario());
        model.addAttribute("turno", turno);

        return "recepcionista/agendar_turno";
    }

    @PostMapping("/agendar_turno")
    public String procesarNuevoTurno(@ModelAttribute("turno") Turno turno,
                                     @RequestParam("servicioId") Long servicioId,
                                     Authentication authentication) {

        Usuario recepcionista = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorRecepcionista(recepcionista.getId());

        ServicioMedico servicio = servicioMedicoService.obtenerPorId(servicioId);

        turno.setServicioMedico(servicio);
        turno.setClinica(clinica);
        turno.setEstado(EstadoTurno.CONFIRMADO);

        turnoService.crearTurno(turno);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

}
