package tfg.psygcv.controller.pet;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_PETS_CREATED;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_PETS_DELETED;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_PETS_UPDATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.service.pet.PetService;

@RequiredArgsConstructor
@RequestMapping("/pets")
@Controller
public class PetController extends BaseController {

  private final PetService petService;

  @GetMapping
  public String listPets(Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
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
  public String savePet(
      @Valid @ModelAttribute Pet pet,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      return "pets/new";
    }
    try {
      AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
      petService.save(pet, currentUser.getId());
      return REDIRECT_MY_PETS_CREATED;
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "pets/new";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("pet", petService.findById(id));
    return "pets/edit";
  }

  @PostMapping("/{id}/edit")
  public String updatePet(
      @PathVariable Long id, @Valid @ModelAttribute Pet pet, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "pets/edit";
    }
    try {
      petService.update(id, pet);
      return REDIRECT_MY_PETS_UPDATED;
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "pets/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deletePet(@PathVariable Long id, RedirectAttributes ra) {
    try {
      petService.deactivate(id);
      return REDIRECT_MY_PETS_DELETED;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/pets";
    }
  }
}
