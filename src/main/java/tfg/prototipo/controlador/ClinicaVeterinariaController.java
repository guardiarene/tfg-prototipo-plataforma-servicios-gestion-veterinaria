package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.ClinicaVeterinariaService;
import tfg.prototipo.servicio.UsuarioService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/clinicas_veterinarias")
public class ClinicaVeterinariaController {

    private final ClinicaVeterinariaService clinicaVeterinariaService;

    private final UsuarioService usuarioService;

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("clinica", new ClinicaVeterinaria());

        return "clinicas_veterinarias/registrarse";
    }

    @PostMapping("/registrarse")
    public String registrarVeterinarioYClinica(@ModelAttribute Usuario usuario, @RequestParam String clinicaNombre, @RequestParam String clinicaTelefono, @RequestParam String clinicaDireccion, @RequestParam String clinicaEmail) {
        usuario.setTipoRol(Rol.VETERINARIO);
        usuarioService.registrar(usuario);

        ClinicaVeterinaria clinica = new ClinicaVeterinaria();
        clinica.setNombre(clinicaNombre);
        clinica.setTelefono(clinicaTelefono);
        clinica.setDireccion(clinicaDireccion);
        clinica.setEmail(clinicaEmail);
        clinica.setVeterinario(usuario);

        clinicaVeterinariaService.registrar(clinica);
        return "redirect:/usuarios/login";
    }

    @GetMapping("/buscar_veterinarias")
    public String buscarVeterinarias(@RequestParam(value = "query", required = false) String query, Model model) {
        List<ClinicaVeterinaria> clinicasVeterinarias;

        if (query != null && !query.isEmpty()) {
            clinicasVeterinarias = clinicaVeterinariaService.buscarPorNombre(query);
        } else {
            clinicasVeterinarias = clinicaVeterinariaService.obtenerTodas();
        }

        model.addAttribute("clinicasVeterinarias", clinicasVeterinarias);
        return "interfaz_cliente";
    }

    @GetMapping("/{id}")
    public String mostrarDetalleClinica(@PathVariable Long id, Model model) {
        Optional<ClinicaVeterinaria> clinicaOptional = clinicaVeterinariaService.obtenerPorId(id);

        if (clinicaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clínica no encontrada");
        }

        ClinicaVeterinaria clinica = clinicaOptional.get();
        model.addAttribute("clinicaVeterinaria", clinica);
        return "clinicas_veterinarias/detalle_clinica";
    }

}
