package tfg.psygcv.controller.pet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.PetServiceInterface;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_PETS;

@RequiredArgsConstructor
@RequestMapping("/pets")
@Controller
public class PetController extends BaseController {

    private final PetServiceInterface petService;

    @GetMapping
    public String listPets(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        model.addAttribute("pets", petService.findByOwnerId(currentUser.getId()));
        return "pets/list";
    }

    @GetMapping("/{id}")
    public String showPetDetails(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        return "pets/details";
    }

    @GetMapping("/new")
    public String showNewPetForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "pets/new";
    }

    @PostMapping("/new")
    public String savePet(@Valid @ModelAttribute Pet pet,
                          BindingResult result,
                          Authentication authentication) {
        if (result.hasErrors()) {
            return "pets/new";
        }
        User currentUser = getCurrentUser(authentication);
        petService.save(pet, currentUser.getId());
        return REDIRECT_MY_PETS;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        return "pets/edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePet(@PathVariable Long id,
                            @Valid @ModelAttribute Pet pet,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "pets/edit";
        }
        petService.update(id, pet);
        return REDIRECT_MY_PETS;
    }

    @PostMapping("/{id}/delete")
    public String deletePet(@PathVariable Long id) {
        petService.deactivate(id);
        return REDIRECT_MY_PETS;
    }

}
