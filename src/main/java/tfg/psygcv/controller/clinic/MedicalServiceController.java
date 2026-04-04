package tfg.psygcv.controller.clinic;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_SERVICES_CREATED;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_SERVICES_DELETED;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_SERVICES_UPDATED;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.entity.clinic.MedicalService;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.interfaces.MedicalServiceServiceInterface;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/medical-services")
@Controller
public class MedicalServiceController extends BaseController {

  private final MedicalServiceServiceInterface medicalServiceService;
  private final VeterinaryClinicServiceInterface veterinaryClinicService;
  private final UserServiceInterface userService;

  @GetMapping
  public String listServices(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute("clinic", clinic);
    model.addAttribute("services", medicalServiceService.findByVeterinarianClinic(veterinarian));
    return "medical_services/list";
  }

  @GetMapping("/new")
  public String showNewServiceForm(Model model, Authentication authentication) {
    User user = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(user.getId());
    model.addAttribute("medicalService", new MedicalService());
    model.addAttribute("clinicId", clinic.getId());
    return "medical_services/new";
  }

  @PostMapping("/new")
  public String saveService(
      @ModelAttribute("medicalService") MedicalService medicalService,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId,
      Model model) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("clinicId", clinicId);
        return "medical_services/new";
      }
      medicalServiceService.save(medicalService, clinicId);
      return REDIRECT_MY_SERVICES_CREATED;
    } catch (Exception e) {
      model.addAttribute("error", "Error al guardar el servicio: " + e.getMessage());
      model.addAttribute("clinicId", clinicId);
      return "medical_services/new";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
    VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
    model.addAttribute(
        "medicalService", medicalServiceService.findByIdAndValidateClinic(id, clinic.getId()));
    model.addAttribute("clinicId", clinic.getId());
    return "medical_services/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateService(
      @PathVariable Long id,
      @ModelAttribute("medicalService") MedicalService medicalService,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId,
      Model model) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("clinicId", clinicId);
        return "medical_services/edit";
      }
      medicalServiceService.update(id, medicalService, clinicId);
      return REDIRECT_MY_SERVICES_UPDATED;
    } catch (Exception e) {
      model.addAttribute("error", "Error al actualizar el servicio: " + e.getMessage());
      model.addAttribute("clinicId", clinicId);
      return "medical_services/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteService(
      @PathVariable Long id, Authentication authentication, RedirectAttributes ra) {
    try {
      User veterinarian = getCurrentUser(authentication, userService);
      VeterinaryClinic clinic = veterinaryClinicService.findByVeterinarianId(veterinarian.getId());
      medicalServiceService.deactivate(id, clinic.getId());
      return REDIRECT_MY_SERVICES_DELETED;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/medical-services";
    }
  }
}
