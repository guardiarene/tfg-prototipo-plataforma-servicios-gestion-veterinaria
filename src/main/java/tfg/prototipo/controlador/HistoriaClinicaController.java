package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.prototipo.modelo.Anamnesis;
import tfg.prototipo.modelo.HistoriaClinica;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.HistoriaClinicaService;
import tfg.prototipo.servicio.MascotaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/historia_clinica")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;
    private final MascotaService mascotaService;

    @GetMapping("/{id}")
    public String mostrarDetalleHistoria(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("historia", historiaClinicaService.obtenerHistoriaCompletaPorId(id));
        Usuario usuario = (Usuario) authentication.getPrincipal();

        return usuario.getTipoRol() == Rol.VETERINARIO
                ? "historias_clinicas/detalle_historia_clinica"
                : "mascotas/historia_clinica";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model, Authentication authentication) {
        HistoriaClinica historia = new HistoriaClinica();
        historia.setTratamientos(new ArrayList<>());
        historia.setDiagnosticos(new ArrayList<>());
        historia.setAnamnesis(new Anamnesis());
        historia.getAnamnesis().setVacunas(new ArrayList<>());

        Usuario veterinario = (Usuario) authentication.getPrincipal();
        model.addAttribute("historia", historia);
        model.addAttribute("mascotas", mascotaService.obtenerMascotasConTurnosEnClinica(veterinario));

        return "historias_clinicas/nueva_historia_clinica";
    }

    @PostMapping("/nueva")
    public String guardarHistoriaClinica(@ModelAttribute("historia") HistoriaClinica historia,
                                         Authentication authentication) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();
        historia.setVeterinario(veterinario);
        historia.setFecha(LocalDate.now());

        historiaClinicaService.guardarHistoriaClinica(historia, veterinario);
        return "redirect:/veterinario/interfaz_veterinario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           Model model,
                                           Authentication authentication) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();
        HistoriaClinica historia = historiaClinicaService.obtenerHistoriaCompletaParaEdicion(id);

        model.addAttribute("historia", historia);
        model.addAttribute("mascotas", List.of(historia.getPaciente()));

        return "historias_clinicas/editar_historia_clinica";
    }

    @PostMapping("/editar/{id}")
    public String actualizarHistoriaClinica(@PathVariable Long id,
                                            @ModelAttribute("historia") HistoriaClinica historiaActualizada,
                                            Authentication authentication) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();
        historiaClinicaService.actualizarHistoriaClinica(id, historiaActualizada, veterinario);

        return "redirect:/historia_clinica/" + id;
    }

}
