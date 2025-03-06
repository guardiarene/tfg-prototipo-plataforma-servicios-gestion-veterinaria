package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.HistoriaClinica;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.ClinicaVeterinariaService;
import tfg.prototipo.servicio.HistoriaClinicaService;
import tfg.prototipo.servicio.UsuarioService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/veterinario")
public class VeterinarioController {

    private final HistoriaClinicaService historiaClinicaService;

    private final UsuarioService usuarioService;

    private final ClinicaVeterinariaService clinicaService;

    @GetMapping("/interfaz_veterinario")
    public String mostrarHistorias(Model model,
                                   @AuthenticationPrincipal Usuario veterinario) {
        ClinicaVeterinaria clinicaVeterinaria = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());
        List<HistoriaClinica> historias = historiaClinicaService.obtenerHistoriasClinicasPorVeterinario(veterinario);

        model.addAttribute("historias", historias);

        return "veterinario/interfaz_veterinario";
    }

    @GetMapping("/mi_clinica")
    public String mostrarDatosClinica(Model model, Authentication authentication) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());

        model.addAttribute("clinica", clinica);
        model.addAttribute("veterinario", veterinario);

        return "veterinario/mi_clinica";
    }

    @GetMapping("/mi_clinica/actualizar_clinica")
    public String mostrarFormularioDeActualizacion(Model model, Authentication authentication) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinario.getId());

        model.addAttribute("veterinario", veterinario);
        model.addAttribute("clinica", clinica);

        return "veterinario/actualizar_clinica";
    }

    @PostMapping("/mi_clinica/actualizar_veterinario")
    public String actualizarDatosVeterinario(@ModelAttribute("veterinario") Usuario veterinario, Authentication authentication) {
        Usuario veterinarioAutenticado = (Usuario) authentication.getPrincipal();
        veterinario.setId(veterinarioAutenticado.getId());
        veterinario.setTipoRol(Rol.VETERINARIO);

        usuarioService.actualizar(veterinario);
        return "redirect:/veterinario/mi_clinica";
    }

    @PostMapping("/mi_clinica/actualizar_clinica")
    public String actualizarDatosClinica(@ModelAttribute("clinica") ClinicaVeterinaria clinica, Authentication authentication) {
        Usuario veterinarioAutenticado = (Usuario) authentication.getPrincipal();
        clinica.setVeterinario(veterinarioAutenticado);

        clinicaService.actualizar(clinica);
        return "redirect:/veterinario/mi_clinica";
    }

    @PostMapping("/mi_clinica/darse_de_baja")
    public String eliminarVeterinarioYClinica(Authentication authentication) {
        Usuario veterinarioAutenticado = (Usuario) authentication.getPrincipal();
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorVeterinario(veterinarioAutenticado.getId());

        clinicaService.darDeBaja(clinica.getId());
        usuarioService.darseDeBaja(veterinarioAutenticado.getId());
        return "redirect:/usuarios/login?logout";
    }

}
