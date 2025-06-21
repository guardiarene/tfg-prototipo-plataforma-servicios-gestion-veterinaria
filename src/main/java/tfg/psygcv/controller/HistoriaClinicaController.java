package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.medical.Anamnesis;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.MedicalRecordServiceImpl;
import tfg.psygcv.service.impl.PetServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/historia_clinica")
public class HistoriaClinicaController {

    private final MedicalRecordServiceImpl medicalRecordServiceImpl;
    private final PetServiceImpl petServiceImpl;

    @GetMapping("/{id}")
    public String mostrarDetalleHistoria(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("historia", medicalRecordServiceImpl.findCompleteForEditing(id));
        User user = (User) authentication.getPrincipal();

        return user.getRole() == Role.VETERINARIAN
                ? "historias_clinicas/detalle_historia_clinica"
                : "mascotas/historia_clinica";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model, Authentication authentication) {
        MedicalRecord historia = new MedicalRecord();
        historia.setTreatments(new ArrayList<>());
        historia.setDiagnostics(new ArrayList<>());
        historia.setAnamnesis(new Anamnesis());
        historia.getAnamnesis().setVaccines(new ArrayList<>());

        User veterinario = (User) authentication.getPrincipal();
        model.addAttribute("historia", historia);
        model.addAttribute("mascotas", petServiceImpl.findPetsWithAppointmentsInClinics(veterinario));

        return "historias_clinicas/nueva_historia_clinica";
    }

    @PostMapping("/nueva")
    public String guardarHistoriaClinica(@ModelAttribute("historia") MedicalRecord historia,
                                         Authentication authentication) {
        User veterinario = (User) authentication.getPrincipal();
        historia.setVeterinarian(veterinario);
        historia.setDate(LocalDate.now());

        medicalRecordServiceImpl.save(historia, veterinario);
        return "redirect:/veterinario/interfaz_veterinario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           Model model,
                                           Authentication authentication) {
        User veterinario = (User) authentication.getPrincipal();
        MedicalRecord historia = medicalRecordServiceImpl.findCompleteForEditing(id);

        model.addAttribute("historia", historia);
        model.addAttribute("mascotas", List.of(historia.getPet()));

        return "historias_clinicas/editar_historia_clinica";
    }

    @PostMapping("/editar/{id}")
    public String actualizarHistoriaClinica(@PathVariable Long id,
                                            @ModelAttribute("historia") MedicalRecord historiaActualizada,
                                            Authentication authentication) {
        User veterinario = (User) authentication.getPrincipal();
        medicalRecordServiceImpl.update(id, historiaActualizada, veterinario);

        return "redirect:/historia_clinica/" + id;
    }

}
