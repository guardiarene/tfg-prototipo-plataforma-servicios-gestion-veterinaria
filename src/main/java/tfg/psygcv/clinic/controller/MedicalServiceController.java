package tfg.psygcv.clinic.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_SERVICES_CREATED;
import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_SERVICES_DELETED;
import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_SERVICES_UPDATED;

import jakarta.validation.Valid;
import java.util.List;
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
import tfg.psygcv.clinic.dto.request.CreateMedicalServiceRequest;
import tfg.psygcv.clinic.dto.request.UpdateMedicalServiceRequest;
import tfg.psygcv.clinic.dto.response.MedicalServiceResponse;
import tfg.psygcv.clinic.mapper.MedicalServiceMapper;
import tfg.psygcv.clinic.service.MedicalServiceService;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/medical-services")
@Controller
public class MedicalServiceController extends BaseController {

  private final MedicalServiceService medicalServiceService;
  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping
  public String listServices(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    List<MedicalServiceResponse> services =
        medicalServiceService.findByVeterinarianClinic(veterinarianId).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList();
    model.addAttribute("services", services);
    return "medical_services/list";
  }

  @GetMapping("/new")
  public String showNewServiceForm(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    Long clinicId = veterinaryClinicService.findClinicIdByVeterinarianId(veterinarianId);
    model.addAttribute("medicalService", new CreateMedicalServiceRequest());
    model.addAttribute("clinicId", clinicId);
    return "medical_services/new";
  }

  @PostMapping("/new")
  public String saveService(
      @Valid @ModelAttribute("medicalService") CreateMedicalServiceRequest request,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId,
      Model model) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("clinicId", clinicId);
        return "medical_services/new";
      }
      medicalServiceService.save(MedicalServiceMapper.toCreateCommand(request, clinicId));
      return REDIRECT_MY_SERVICES_CREATED;
    } catch (Exception e) {
      model.addAttribute("error", "Error al guardar el servicio: " + e.getMessage());
      model.addAttribute("clinicId", clinicId);
      return "medical_services/new";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    Long clinicId = veterinaryClinicService.findClinicIdByVeterinarianId(veterinarianId);
    model.addAttribute(
        "medicalService",
        MedicalServiceMapper.toUpdateRequest(
            medicalServiceService.findByIdAndValidateClinic(id, clinicId)));
    model.addAttribute("medicalServiceId", id);
    model.addAttribute("clinicId", clinicId);
    return "medical_services/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateService(
      @PathVariable Long id,
      @Valid @ModelAttribute("medicalService") UpdateMedicalServiceRequest request,
      BindingResult result,
      @RequestParam("clinicId") Long clinicId,
      Model model) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("medicalServiceId", id);
        model.addAttribute("clinicId", clinicId);
        return "medical_services/edit";
      }
      medicalServiceService.update(id, MedicalServiceMapper.toUpdateCommand(request), clinicId);
      return REDIRECT_MY_SERVICES_UPDATED;
    } catch (Exception e) {
      model.addAttribute("error", "Error al actualizar el servicio: " + e.getMessage());
      model.addAttribute("medicalServiceId", id);
      model.addAttribute("clinicId", clinicId);
      return "medical_services/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteService(
      @PathVariable Long id, Authentication authentication, RedirectAttributes ra) {
    try {
      Long veterinarianId = getAuthenticatedUser(authentication).getId();
      Long clinicId = veterinaryClinicService.findClinicIdByVeterinarianId(veterinarianId);
      medicalServiceService.deactivate(id, clinicId);
      return REDIRECT_MY_SERVICES_DELETED;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/medical-services";
    }
  }
}
