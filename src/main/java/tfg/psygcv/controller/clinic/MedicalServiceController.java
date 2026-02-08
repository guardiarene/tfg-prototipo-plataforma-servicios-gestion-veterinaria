package tfg.psygcv.controller.clinic;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_SERVICES;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.MedicalServiceServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/medical-services")
@Controller
public class MedicalServiceController extends BaseController {

  private final MedicalServiceServiceInterface medicalServiceService;

  private final VeterinaryClinicServiceInterface veterinaryClinicService;

  @GetMapping
  public String listServices(Model model, @AuthenticationPrincipal User veterinarian) {
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("clinic", clinic);
    model.addAttribute("services", medicalServiceService.findByVeterinarianClinic(veterinarian));
    return "medical_services/list";
  }

  @GetMapping("/new")
  public String showNewServiceForm(Model model, @AuthenticationPrincipal User user) {
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(user.getId());
    model.addAttribute("medicalService", new MedicalService());
    model.addAttribute("clinicId", clinic.getId());
    return "medical_services/new";
  }

  @PostMapping("/new")
  public String saveService(
      @Valid @ModelAttribute MedicalService medicalService,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId) {
    if (result.hasErrors()) {
      return "medical_services/new";
    }
    medicalServiceService.save(medicalService, clinicId);
    return REDIRECT_MY_SERVICES;
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(
      @PathVariable Long id, Model model, @AuthenticationPrincipal User veterinarian) {
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute(
        "medicalService", medicalServiceService.findByIdAndValidateClinic(id, clinic.getId()));
    model.addAttribute("clinicId", clinic.getId());
    return "medical_services/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateService(
      @PathVariable Long id,
      @Valid @ModelAttribute MedicalService medicalService,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId) {
    if (result.hasErrors()) {
      return "medical_services/edit";
    }
    medicalServiceService.update(id, medicalService, clinicId);
    return REDIRECT_MY_SERVICES;
  }

  @PostMapping("/{id}/delete")
  public String deleteService(@PathVariable Long id, @AuthenticationPrincipal User veterinarian) {
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    medicalServiceService.deactivate(id, clinic.getId());
    return REDIRECT_MY_SERVICES;
  }
}
