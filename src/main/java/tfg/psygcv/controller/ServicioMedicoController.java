package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.user.User;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.service.impl.MedicalServiceServiceImpl;
import tfg.psygcv.service.impl.VeterinaryClinicServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/servicios")
public class ServicioMedicoController {

    private final MedicalServiceServiceImpl medicalServiceServiceImpl;

    private final VeterinaryClinicServiceImpl clinicaService;

    @RequestMapping("/mis_servicios")
    public String listarServicios(Model model, @AuthenticationPrincipal User veterinario) {
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());
        List<MedicalService> servicios = medicalServiceServiceImpl.findByVeterinarianClinic(veterinario);

        model.addAttribute("clinica", clinica);
        model.addAttribute("servicios", servicios);

        return "servicios/mis_servicios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model,
                                         @AuthenticationPrincipal User user) {
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(user.getId());

        model.addAttribute("servicioMedico", new MedicalService());
        model.addAttribute("clinicaId", clinica.getId());

        return "servicios/nuevo_servicio";
    }

    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute MedicalService medicalService,
                                  @RequestParam("clinicaId") Long clinicaId) {
        medicalServiceServiceImpl.save(medicalService, clinicaId);
        return "redirect:/servicios/mis_servicios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           Model model,
                                           @AuthenticationPrincipal User veterinario) {
        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());
        MedicalService servicio = medicalServiceServiceImpl.findByIdAndValidateClinic(id, clinica.getId());

        model.addAttribute("servicioMedico", servicio);
        model.addAttribute("clinicaId", clinica.getId());

        return "servicios/editar_servicio";
    }

    @PostMapping("/update/{id}")
    public String actualizarServicio(@PathVariable Long id,
                                     @ModelAttribute MedicalService servicioActualizado,
                                     @AuthenticationPrincipal User veterinario) {

        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());

        medicalServiceServiceImpl.update(id, servicioActualizado, clinica.getId());
        return "redirect:/servicios/mis_servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id,
                                   @AuthenticationPrincipal User veterinario) {

        VeterinaryClinic clinica = clinicaService.findByVeterinarianId(veterinario.getId());

        medicalServiceServiceImpl.deactivate(id, clinica.getId());
        return "redirect:/servicios/mis_servicios";
    }

}
