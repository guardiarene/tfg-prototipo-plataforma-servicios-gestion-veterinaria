package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.PetServiceImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    private final PetServiceImpl petServiceImpl;

    @GetMapping("/mis_mascotas")
    public String listarMascotas(Model model, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        model.addAttribute("mascotas", petServiceImpl.findByOwnerId(cliente.getId()));

        return "mascotas/mis_mascotas";
    }

    @GetMapping("/{id}")
    public String mostrarInformacionMascota(@PathVariable Long id, Model model) {
        Pet pet = petServiceImpl.findById(id);
        model.addAttribute("pet", pet);

        return "mascotas/informacion_mascota";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("mascota", new Pet());
        return "mascotas/nueva_mascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute Pet pet, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        petServiceImpl.save(pet, cliente.getId());
        return "redirect:/mascotas/mis_mascotas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("mascota", petServiceImpl.findById(id));
        return "mascotas/editar_mascota";
    }

    @PostMapping("/update/{id}")
    public String actualizarMascota(@PathVariable Long id, @ModelAttribute Pet pet) {
        petServiceImpl.update(id, pet);
        return "redirect:/mascotas/mis_mascotas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id) {
        petServiceImpl.deactivate(id);
        return "redirect:/mascotas/mis_mascotas";
    }

}
