package tfg.psygcv.controller.clinic;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_LOGIN;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.service.clinic.VeterinaryClinicService;

@RequiredArgsConstructor
@RequestMapping("/clinics")
@Controller
public class VeterinaryClinicController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    return "clinics/register";
  }

  @PostMapping("/register")
  public String registerVeterinarianAndClinic(
      @RequestParam Map<String, String> params,
      Model model,
      RedirectAttributes redirectAttributes) {
    String password = params.get("userPassword");
    String confirmPassword = params.get("confirmPassword");
    if (password == null || !password.equals(confirmPassword)) {
      model.addAttribute("error", "Las contraseñas no coinciden.");
      params.remove("userPassword");
      params.remove("confirmPassword");
      model.addAllAttributes(params);
      return "clinics/register";
    }
    try {
      veterinaryClinicService.registerClinicWithVeterinarian(params);
      redirectAttributes.addAttribute("success", true);
      return REDIRECT_LOGIN;
    } catch (Exception e) {
      model.addAttribute("error", "Error al registrar: " + e.getMessage());
      params.remove("userPassword");
      params.remove("confirmPassword");
      model.addAllAttributes(params);
      return "clinics/register";
    }
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
