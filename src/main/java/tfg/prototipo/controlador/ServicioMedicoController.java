package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.ServicioMedico;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.ClinicaVeterinariaService;
import tfg.prototipo.servicio.ServicioMedicoService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/servicios")
public class ServicioMedicoController {

    private final ServicioMedicoService servicioMedicoService;

    private final ClinicaVeterinariaService clinicaService;

    @RequestMapping("/mis_servicios")
    public String listarServicios(Model model, @AuthenticationPrincipal Usuario veterinario) {
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());
        List<ServicioMedico> servicios = servicioMedicoService.obtenerServiciosPorClinicaVeterinario(veterinario);

        model.addAttribute("clinica", clinica);
        model.addAttribute("servicios", servicios);

        return "servicios/mis_servicios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model,
                                         @AuthenticationPrincipal Usuario usuario) {
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(usuario.getId());

        model.addAttribute("servicioMedico", new ServicioMedico());
        model.addAttribute("clinicaId", clinica.getId());

        return "servicios/nuevo_servicio";
    }

    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute ServicioMedico servicioMedico,
                                  @RequestParam("clinicaId") Long clinicaId) {
        servicioMedicoService.crearServicio(servicioMedico, clinicaId);
        return "redirect:/servicios/mis_servicios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           Model model,
                                           @AuthenticationPrincipal Usuario veterinario) {
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());
        ServicioMedico servicio = servicioMedicoService.obtenerServicioYValidarClinica(id, clinica.getId());

        model.addAttribute("servicioMedico", servicio);
        model.addAttribute("clinicaId", clinica.getId());

        return "servicios/editar_servicio";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarServicio(@PathVariable Long id,
                                     @ModelAttribute ServicioMedico servicioActualizado,
                                     @AuthenticationPrincipal Usuario veterinario) {

        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());

        servicioMedicoService.actualizarServicio(id, servicioActualizado, clinica.getId());
        return "redirect:/servicios/mis_servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id,
                                   @AuthenticationPrincipal Usuario veterinario) {

        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());

        servicioMedicoService.eliminarServicio(id, clinica.getId());
        return "redirect:/servicios/mis_servicios";
    }

}
