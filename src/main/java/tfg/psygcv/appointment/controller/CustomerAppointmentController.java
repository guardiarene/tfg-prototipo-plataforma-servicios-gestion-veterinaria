package tfg.psygcv.appointment.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_MY_APPOINTMENTS;
import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_RECEPTIONIST_DASHBOARD;

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
import tfg.psygcv.appointment.dto.request.CreateClientAppointmentRequest;
import tfg.psygcv.appointment.dto.response.AppointmentSummaryResponse;
import tfg.psygcv.appointment.mapper.AppointmentMapper;
import tfg.psygcv.appointment.service.AppointmentService;
import tfg.psygcv.clinic.mapper.MedicalServiceMapper;
import tfg.psygcv.clinic.mapper.VeterinaryClinicMapper;
import tfg.psygcv.clinic.service.MedicalServiceService;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.pet.mapper.PetMapper;
import tfg.psygcv.pet.service.PetService;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.entity.Role;

@RequiredArgsConstructor
@RequestMapping("/appointments")
@Controller
public class CustomerAppointmentController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;
  private final MedicalServiceService medicalServiceService;
  private final PetService petService;
  private final AppointmentService appointmentService;

  @GetMapping
  public String listAppointments(Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    List<AppointmentSummaryResponse> appointments =
        appointmentService.findByCustomerId(currentUser.getId()).stream()
            .map(AppointmentMapper::toSummary)
            .toList();
    model.addAttribute("appointments", appointments);
    return "appointments/list";
  }

  @GetMapping("/new")
  public String showNewAppointmentForm(
      @RequestParam("clinicId") Long clinicId, Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    model.addAttribute(
        "clinic", VeterinaryClinicMapper.toSummary(veterinaryClinicService.findById(clinicId)));
    model.addAttribute(
        "services",
        medicalServiceService.findByClinicId(clinicId).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList());
    model.addAttribute(
        "pets",
        petService.findByOwnerId(currentUser.getId()).stream().map(PetMapper::toSummary).toList());
    return "appointments/new";
  }

  @PostMapping("/new")
  public String createAppointment(
      @Valid @ModelAttribute CreateClientAppointmentRequest request,
      BindingResult result,
      Authentication authentication,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      ra.addFlashAttribute("error", "Datos inválidos. Verifique los campos.");
      return "redirect:/appointments/new?clinicId=" + request.getClinicId();
    }
    try {
      Long currentUserId = getAuthenticatedUser(authentication).getId();
      appointmentService.createClientAppointment(
          request.getDate(),
          request.getPetId(),
          request.getServiceId(),
          request.getClinicId(),
          currentUserId);
      return REDIRECT_MY_APPOINTMENTS;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/appointments/new?clinicId=" + request.getClinicId();
    }
  }

  @GetMapping("/{id}")
  public String showAppointmentDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    var appointmentEntity = appointmentService.findWithDetails(id);
    model.addAttribute("appointment", AppointmentMapper.toResponse(appointmentEntity));
    model.addAttribute(
        "veterinarianName", appointmentService.findVeterinarianName(appointmentEntity));
    model.addAttribute("role", currentUser.getRole().name());
    return "appointments/details";
  }

  @PostMapping("/{id}/cancel")
  public String cancelAppointment(
      @PathVariable Long id, Authentication authentication, RedirectAttributes ra) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    try {
      appointmentService.cancel(id);
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    if (currentUser.getRole() == Role.RECEPTIONIST) {
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    }
    return REDIRECT_MY_APPOINTMENTS;
  }
}
