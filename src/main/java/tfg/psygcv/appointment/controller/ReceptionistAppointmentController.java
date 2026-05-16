package tfg.psygcv.appointment.controller;

import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_RECEPTIONIST_DASHBOARD;
import static tfg.psygcv.shared.constant.RouteConstant.REDIRECT_RECEPTIONIST_RESCHEDULED;

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
import tfg.psygcv.appointment.dto.request.RescheduleAppointmentRequest;
import tfg.psygcv.appointment.dto.request.ScheduleAppointmentRequest;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.appointment.mapper.AppointmentMapper;
import tfg.psygcv.appointment.service.AppointmentService;
import tfg.psygcv.clinic.mapper.MedicalServiceMapper;
import tfg.psygcv.clinic.service.MedicalServiceService;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.pet.mapper.PetMapper;
import tfg.psygcv.pet.service.PetService;
import tfg.psygcv.shared.controller.BaseController;
import tfg.psygcv.user.mapper.UserMapper;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/appointments")
@Controller
public class ReceptionistAppointmentController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;
  private final MedicalServiceService medicalServiceService;
  private final PetService petService;
  private final AppointmentService appointmentService;
  private final UserService userService;

  @GetMapping("/schedule")
  public String showScheduleForm(
      @RequestParam(required = false) Long customerId, Model model, Authentication authentication) {
    Long receptionistId = getAuthenticatedUser(authentication).getId();
    Long clinicId = veterinaryClinicService.findClinicIdByReceptionistId(receptionistId);
    model.addAttribute("customerId", customerId);
    model.addAttribute(
        "customers",
        userService.findActiveCustomers().stream().map(UserMapper::toSummary).toList());
    model.addAttribute(
        "pets",
        customerId != null
            ? petService.findByOwnerId(customerId).stream().map(PetMapper::toSummary).toList()
            : List.of());
    model.addAttribute(
        "services",
        medicalServiceService.findByClinicId(clinicId).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList());
    ScheduleAppointmentRequest scheduleRequest = new ScheduleAppointmentRequest();
    scheduleRequest.setCustomerId(customerId);
    model.addAttribute("scheduleRequest", scheduleRequest);
    return "receptionist/schedule_appointment";
  }

  @PostMapping("/schedule")
  public String scheduleAppointment(
      @Valid @ModelAttribute("scheduleRequest") ScheduleAppointmentRequest request,
      BindingResult result,
      Authentication authentication,
      Model model) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    if (result.hasErrors()) {
      Long clinicId = veterinaryClinicService.findClinicIdByReceptionistId(receptionist.getId());
      populateScheduleModel(model, request.getCustomerId(), clinicId);
      return "receptionist/schedule_appointment";
    }
    try {
      appointmentService.createReceptionistAppointment(
          AppointmentMapper.toScheduleCommand(request), receptionist.getId());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    } catch (Exception e) {
      Long clinicId = veterinaryClinicService.findClinicIdByReceptionistId(receptionist.getId());
      populateScheduleModel(model, request.getCustomerId(), clinicId);
      model.addAttribute("error", e.getMessage());
      return "receptionist/schedule_appointment";
    }
  }

  @GetMapping("/{id}/reschedule")
  public String showRescheduleForm(
      @PathVariable Long id, Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    Long clinicId = veterinaryClinicService.findClinicIdByReceptionistId(receptionist.getId());
    model.addAttribute(
        "appointment", AppointmentMapper.toResponse(appointmentService.findWithDetails(id)));
    model.addAttribute(
        "services",
        medicalServiceService.findByClinicId(clinicId).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList());
    model.addAttribute("rescheduleRequest", new RescheduleAppointmentRequest());
    model.addAttribute("role", receptionist.getRole().name());
    return "receptionist/reschedule_appointment";
  }

  @PostMapping("/{id}/reschedule")
  public String rescheduleAppointment(
      @PathVariable Long id,
      @Valid @ModelAttribute("rescheduleRequest") RescheduleAppointmentRequest request,
      BindingResult result,
      Model model,
      Authentication authentication,
      RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      Long clinicId =
          veterinaryClinicService.findClinicIdByReceptionistId(
              getAuthenticatedUser(authentication).getId());
      model.addAttribute(
          "appointment", AppointmentMapper.toResponse(appointmentService.findWithDetails(id)));
      model.addAttribute(
          "services",
          medicalServiceService.findByClinicId(clinicId).stream()
              .map(MedicalServiceMapper::toResponse)
              .toList());
      return "receptionist/reschedule_appointment";
    }
    try {
      appointmentService.reschedule(id, AppointmentMapper.toRescheduleCommand(request));
      return REDIRECT_RECEPTIONIST_RESCHEDULED;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
          "error", "Error al reprogramar el turno: " + e.getMessage());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    }
  }

  @PostMapping("/{id}/update-status")
  public String updateAppointmentStatus(
      @PathVariable Long id,
      @RequestParam("status") AppointmentStatus status,
      RedirectAttributes ra) {
    try {
      appointmentService.updateStatus(id, status);
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return REDIRECT_RECEPTIONIST_DASHBOARD;
  }

  private void populateScheduleModel(Model model, Long customerId, Long clinicId) {
    model.addAttribute("customerId", customerId);
    model.addAttribute(
        "customers",
        userService.findActiveCustomers().stream().map(UserMapper::toSummary).toList());
    model.addAttribute(
        "pets",
        customerId != null
            ? petService.findByOwnerId(customerId).stream().map(PetMapper::toSummary).toList()
            : List.of());
    model.addAttribute(
        "services",
        medicalServiceService.findByClinicId(clinicId).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList());
  }
}
