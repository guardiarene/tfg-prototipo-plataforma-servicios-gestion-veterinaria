package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.prototipo.modelo.Mascota;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.MascotaService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping("/mis_mascotas")
    public String listarMascotas(Model model, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        model.addAttribute("mascotas", mascotaService.obtenerMascotasPorCliente(cliente.getId()));

        return "mascotas/mis_mascotas";
    }

    @GetMapping("/{id}")
    public String mostrarInformacionMascota(@PathVariable Long id, Model model) {
        Mascota mascota = mascotaService.obtenerMascota(id);
        model.addAttribute("mascota", mascota);

        return "mascotas/informacion_mascota";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("mascota", new Mascota());
        return "mascotas/nueva_mascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute Mascota mascota, Authentication authentication) {
        Usuario cliente = (Usuario) authentication.getPrincipal();
        mascotaService.guardarMascota(mascota, cliente.getId());
        return "redirect:/mascotas/mis_mascotas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("mascota", mascotaService.obtenerMascota(id));
        return "mascotas/editar_mascota";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarMascota(@PathVariable Long id, @ModelAttribute Mascota mascota) {
        mascotaService.actualizarMascota(id, mascota);
        return "redirect:/mascotas/mis_mascotas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return "redirect:/mascotas/mis_mascotas";
    }

}
