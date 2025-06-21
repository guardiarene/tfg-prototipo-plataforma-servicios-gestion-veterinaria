package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.MedicalRecordServiceImpl;
import tfg.psygcv.service.impl.UserServiceImpl;
import tfg.psygcv.service.impl.VeterinaryClinicServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/veterinario")
public class VeterinarioController {

    private final MedicalRecordServiceImpl medicalRecordServiceImpl;

    private final UserServiceImpl userServiceImpl;

    private final VeterinaryClinicServiceImpl clinicaService;

    @GetMapping("/interfaz_veterinario")
    public String mostrarHistorias(Model model,
                                   @AuthenticationPrincipal User veterinario) {
        VeterinaryClinic veterinaryClinic = clinicaService.findByVeterinarianId(veterinario.getId());
        List<MedicalRecord> historias = medicalRecordServiceImpl.findByVeterinarian(veterinario);

        model.addAttribute("historias", historias);

        return "veterinario/interfaz_veterinario";
    }

    @GetMapping("/mi_clinica")
    public String mostrarDatosClinica(Model model, Authentication authentication) {
        User veterinario = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());

        model.addAttribute("clinica", clinica);
        model.addAttribute("veterinario", veterinario);

        return "veterinario/mi_clinica";
    }

    @GetMapping("/mi_clinica/actualizar_clinica")
    public String mostrarFormularioDeActualizacion(Model model, Authentication authentication) {
        User veterinario = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());

        model.addAttribute("veterinario", veterinario);
        model.addAttribute("clinica", clinica);

        return "veterinario/actualizar_clinica";
    }

    @PostMapping("/mi_clinica/actualizar_veterinario")
    public String actualizarDatosVeterinario(@ModelAttribute("veterinario") User veterinario, Authentication authentication) {
        User veterinarioAutenticado = (User) authentication.getPrincipal();
        veterinario.setId(veterinarioAutenticado.getId());
        veterinario.setRole(Role.VETERINARIAN);

        userServiceImpl.update(veterinario);
        return "redirect:/veterinario/mi_clinica";
    }

    @PostMapping("/mi_clinica/actualizar_clinica")
    public String actualizarDatosClinica(@ModelAttribute("clinica") VeterinaryClinic clinica, Authentication authentication) {
        User veterinarioAutenticado = (User) authentication.getPrincipal();
        clinica.setVeterinarian(veterinarioAutenticado);

        clinicaService.update(clinica);
        return "redirect:/veterinario/mi_clinica";
    }

    @PostMapping("/mi_clinica/darse_de_baja")
    public String eliminarVeterinarioYClinica(Authentication authentication) {
        User veterinarioAutenticado = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinarioAutenticado.getId());

        clinicaService.deactivate(clinica.getId());
        userServiceImpl.deactivate(veterinarioAutenticado.getId());
        return "redirect:/usuarios/login?logout";
    }

}
