package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.service.impl.UserServiceImpl;
import tfg.psygcv.service.impl.VeterinaryClinicServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/clinicas_veterinarias")
public class ClinicaVeterinariaController {

    private final VeterinaryClinicServiceImpl veterinaryClinicServiceImpl;

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new User());
        model.addAttribute("clinica", new VeterinaryClinic());

        return "clinicas_veterinarias/registrarse";
    }

    @PostMapping("/registrarse")
    public String registrarVeterinarioYClinica(@ModelAttribute User user, @RequestParam String clinicaNombre, @RequestParam String clinicaTelefono, @RequestParam String clinicaDireccion, @RequestParam String clinicaEmail) {
        user.setRole(Role.VETERINARIAN);
        userServiceImpl.save(user);

        VeterinaryClinic clinica = new VeterinaryClinic();
        clinica.setName(clinicaNombre);
        clinica.setPhone(clinicaTelefono);
        clinica.setAddress(clinicaDireccion);
        clinica.setEmail(clinicaEmail);
        clinica.setVeterinarian(user);

        veterinaryClinicServiceImpl.save(clinica);
        return "redirect:/usuarios/login";
    }

    @GetMapping("/buscar_veterinarias")
    public String buscarVeterinarias(@RequestParam(value = "query", required = false) String query, Model model) {
        List<VeterinaryClinic> clinicasVeterinarias;

        if (query != null && !query.isEmpty()) {
            clinicasVeterinarias = veterinaryClinicServiceImpl.searchByName(query);
        } else {
            clinicasVeterinarias = veterinaryClinicServiceImpl.findAll();
        }

        model.addAttribute("clinicasVeterinarias", clinicasVeterinarias);
        return "interfaz_cliente";
    }

    @GetMapping("/{id}")
    public String mostrarDetalleClinica(@PathVariable Long id, Model model) {
        VeterinaryClinic clinica = veterinaryClinicServiceImpl.findById(id);

        model.addAttribute("clinicaVeterinaria", clinica);
        return "clinicas_veterinarias/detalle_clinica";
    }

}
