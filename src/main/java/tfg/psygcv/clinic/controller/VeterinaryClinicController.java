package tfg.psygcv.clinic.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_LOGIN;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.clinic.dto.request.RegisterClinicRequest;
import tfg.psygcv.clinic.dto.response.VeterinaryClinicResponse;
import tfg.psygcv.clinic.dto.response.VeterinaryClinicSummaryResponse;
import tfg.psygcv.clinic.mapper.VeterinaryClinicMapper;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/clinics")
@Controller
public class VeterinaryClinicController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("registerClinicRequest", new RegisterClinicRequest());
    return "clinics/register";
  }

  @PostMapping("/register")
  public String registerVeterinarianAndClinic(
      @Valid @ModelAttribute("registerClinicRequest") RegisterClinicRequest request,
      BindingResult result,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      return "clinics/register";
    }
    if (!request.getUserPassword().equals(request.getConfirmPassword())) {
      model.addAttribute("error", "Las contraseñas no coinciden.");
      return "clinics/register";
    }
    try {
      veterinaryClinicService.registerClinicWithVeterinarian(
          VeterinaryClinicMapper.toRegisterCommand(request));
      redirectAttributes.addAttribute("success", true);
      return REDIRECT_LOGIN;
    } catch (Exception e) {
      model.addAttribute("error", "Error al registrar: " + e.getMessage());
      return "clinics/register";
    }
  }

  @GetMapping("/search")
  public String searchClinics(
      @RequestParam(value = "q", required = false) String query, Model model) {
    List<VeterinaryClinicSummaryResponse> clinics =
        (query != null && !query.trim().isEmpty())
            ? veterinaryClinicService.searchByName(query).stream()
            .map(VeterinaryClinicMapper::toSummary)
            .toList()
            : veterinaryClinicService.findAll().stream()
            .map(VeterinaryClinicMapper::toSummary)
            .toList();
    model.addAttribute("veterinaryClinics", clinics);
    model.addAttribute("searchQuery", query);
    return "clinics/search_results";
  }

  @GetMapping("/{id}")
  public String showClinicDetails(@PathVariable Long id, Model model) {
    VeterinaryClinicResponse clinic =
        VeterinaryClinicMapper.toResponse(veterinaryClinicService.findById(id));
    model.addAttribute("clinic", clinic);
    return "clinics/details";
  }
}
