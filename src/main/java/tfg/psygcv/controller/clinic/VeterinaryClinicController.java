package tfg.psygcv.controller.clinic;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_LOGIN;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/clinics")
@Controller
public class VeterinaryClinicController extends BaseController {

  private final VeterinaryClinicServiceInterface veterinaryClinicService;

  private final UserServiceInterface userService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("clinic", new VeterinaryClinic());
    return "clinics/register";
  }

  @PostMapping("/register")
  public String registerVeterinarianAndClinic(
      @Valid @ModelAttribute User user,
      BindingResult userResult,
      @Valid @ModelAttribute VeterinaryClinic clinic,
      BindingResult clinicResult) {
    if (userResult.hasErrors() || clinicResult.hasErrors()) {
      return "clinics/register";
    }
    user.setRole(Role.VETERINARIAN);
    userService.save(user);
    clinic.setVeterinarian(user);
    veterinaryClinicService.save(clinic);
    return REDIRECT_LOGIN;
  }

  @GetMapping("/search")
  public String searchClinics(
      @RequestParam(value = "q", required = false) String query, Model model) {
    List<VeterinaryClinic> clinics =
        (query != null && !query.trim().isEmpty())
            ? veterinaryClinicService.searchByName(query)
            : veterinaryClinicService.findAll();
    model.addAttribute("veterinaryClinics", clinics);
    model.addAttribute("searchQuery", query);
    return "clinics/search_results";
  }

  @GetMapping("/{id}")
  public String showClinicDetails(@PathVariable Long id, Model model) {
    model.addAttribute("clinic", veterinaryClinicService.findById(id));
    return "clinics/details";
  }
}
