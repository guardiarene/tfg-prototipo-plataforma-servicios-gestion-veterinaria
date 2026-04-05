package tfg.psygcv.controller.appointment;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_APPOINTMENTS;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_RECEPTIONIST_DASHBOARD;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_RECEPTIONIST_RESCHEDULED;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.appointment.request.RescheduleAppointmentRequest;
import tfg.psygcv.dto.appointment.request.ScheduleAppointmentRequest;
import tfg.psygcv.dto.appointment.response.AppointmentSummaryResponse;
import tfg.psygcv.entity.appointment.AppointmentStatus;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.mapper.appointment.AppointmentMapper;
import tfg.psygcv.mapper.clinic.MedicalServiceMapper;
import tfg.psygcv.mapper.clinic.VeterinaryClinicMapper;
import tfg.psygcv.mapper.pet.PetMapper;
import tfg.psygcv.mapper.user.UserMapper;
import tfg.psygcv.service.appointment.AppointmentService;
import tfg.psygcv.service.appointment.RescheduleAppointmentCommand;
import tfg.psygcv.service.appointment.ScheduleAppointmentCommand;
import tfg.psygcv.service.clinic.MedicalServiceService;
import tfg.psygcv.service.clinic.VeterinaryClinicService;
import tfg.psygcv.service.pet.PetService;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/appointments")
@Controller
public class AppointmentController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;
  private final MedicalServiceService medicalServiceService;
  private final PetService petService;
  private final AppointmentService appointmentService;
  private final UserService userService;

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
      @RequestParam("clinicId") Long clinicId,
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @RequestParam("petId") Long petId,
      @RequestParam("serviceId") Long serviceId,
      Authentication authentication,
      RedirectAttributes ra) {
    try {
      User currentUser = getCurrentUser(authentication, userService);
      appointmentService.createClientAppointment(date, petId, serviceId, clinicId, currentUser);
      return REDIRECT_MY_APPOINTMENTS;
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/appointments/new?clinicId=" + clinicId;
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

  @GetMapping("/{id}/reschedule")
  public String showRescheduleForm(
      @PathVariable Long id, Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    model.addAttribute(
        "appointment", AppointmentMapper.toResponse(appointmentService.findWithDetails(id)));
    model.addAttribute(
        "services",
        medicalServiceService.findByClinicId(clinic.getId()).stream()
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
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
      model.addAttribute(
          "appointment", AppointmentMapper.toResponse(appointmentService.findWithDetails(id)));
      model.addAttribute(
          "services",
          medicalServiceService.findByClinicId(clinic.getId()).stream()
              .map(MedicalServiceMapper::toResponse)
              .toList());
      return "receptionist/reschedule_appointment";
    }
    try {
      appointmentService.reschedule(
          id,
          RescheduleAppointmentCommand.builder()
              .date(request.getDate())
              .time(request.getTime())
              .medicalServiceId(request.getMedicalServiceId())
              .build());
      return REDIRECT_RECEPTIONIST_RESCHEDULED;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
          "error", "Error al reprogramar el turno: " + e.getMessage());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    }
  }

  @GetMapping("/schedule")
  public String showScheduleForm(
      @RequestParam(required = false) Long customerId, Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
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
        medicalServiceService.findByClinicId(clinic.getId()).stream()
            .map(MedicalServiceMapper::toResponse)
            .toList());
    model.addAttribute("scheduleRequest", new ScheduleAppointmentRequest());
    return "receptionist/schedule_appointment";
  }

  @PostMapping("/schedule")
  public String scheduleAppointment(
      @Valid @ModelAttribute("scheduleRequest") ScheduleAppointmentRequest request,
      BindingResult result,
      @RequestParam("customerId") Long customerId,
      @RequestParam("serviceId") Long serviceId,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
      model.addAttribute("customerId", customerId);
      model.addAttribute(
          "customers",
          userService.findActiveCustomers().stream().map(UserMapper::toSummary).toList());
      model.addAttribute(
          "pets", petService.findByOwnerId(customerId).stream().map(PetMapper::toSummary).toList());
      model.addAttribute(
          "services",
          medicalServiceService.findByClinicId(clinic.getId()).stream()
              .map(MedicalServiceMapper::toResponse)
              .toList());
      return "receptionist/schedule_appointment";
    }
    try {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      appointmentService.createReceptionistAppointment(
          ScheduleAppointmentCommand.builder()
              .date(request.getDate())
              .time(request.getTime())
              .petId(request.getPetId())
              .build(),
          customerId,
          serviceId,
          receptionist.getId());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    } catch (Exception e) {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
      model.addAttribute("customerId", customerId);
      model.addAttribute(
          "customers",
          userService.findActiveCustomers().stream().map(UserMapper::toSummary).toList());
      model.addAttribute(
          "pets", petService.findByOwnerId(customerId).stream().map(PetMapper::toSummary).toList());
      model.addAttribute(
          "services",
          medicalServiceService.findByClinicId(clinic.getId()).stream()
              .map(MedicalServiceMapper::toResponse)
              .toList());
      model.addAttribute("error", e.getMessage());
      return "receptionist/schedule_appointment";
    }
  }
}
