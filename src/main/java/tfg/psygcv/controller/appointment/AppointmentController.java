package tfg.psygcv.controller.appointment;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_APPOINTMENTS;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_RECEPTIONIST_DASHBOARD;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_RECEPTIONIST_RESCHEDULED;

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
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.appointment.AppointmentStatus;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.interfaces.AppointmentServiceInterface;
import tfg.psygcv.service.interfaces.MedicalServiceServiceInterface;
import tfg.psygcv.service.interfaces.PetServiceInterface;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/appointments")
@Controller
public class AppointmentController extends BaseController {

  private final VeterinaryClinicServiceInterface veterinaryClinicService;
  private final MedicalServiceServiceInterface medicalServiceService;
  private final PetServiceInterface petService;
  private final AppointmentServiceInterface appointmentService;
  private final UserServiceInterface userService;

  @GetMapping
  public String listAppointments(Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    model.addAttribute("appointments", appointmentService.findByCustomerId(currentUser.getId()));
    return "appointments/list";
  }

  @GetMapping("/new")
  public String showNewAppointmentForm(
      @RequestParam("clinicId") Long clinicId, Model model, Authentication authentication) {
    AuthenticatedUser currentUser = getAuthenticatedUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findById(clinicId);
    model.addAttribute("clinic", clinic);
    model.addAttribute("services", medicalServiceService.findByClinicId(clinicId));
    model.addAttribute("pets", petService.findByOwnerId(currentUser.getId()));
    model.addAttribute("appointment", new Appointment());
    return "appointments/new";
  }

  @PostMapping("/new")
  public String createAppointment(
      @RequestParam("clinicId") Long clinicId,
      @RequestParam("date") String date,
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
    Appointment appointment = appointmentService.findWithDetails(id);
    model.addAttribute("appointment", appointment);
    model.addAttribute("veterinarianName", appointmentService.findVeterinarianName(appointment));
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
    populateRescheduleModel(id, model, receptionist);
    return "receptionist/reschedule_appointment";
  }

  @PostMapping("/{id}/reschedule")
  public String rescheduleAppointment(
      @PathVariable Long id,
      @ModelAttribute("appointment") Appointment appointment,
      BindingResult result,
      Model model,
      Authentication authentication,
      RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      populateRescheduleModel(id, model, receptionist);
      return "receptionist/reschedule_appointment";
    }
    try {
      appointmentService.reschedule(id, appointment);
      return REDIRECT_RECEPTIONIST_RESCHEDULED;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
          "error", "Error al reprogramar el turno: " + e.getMessage());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    }
  }

  private void populateRescheduleModel(Long id, Model model, AuthenticatedUser receptionist) {
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    model.addAttribute("appointment", appointmentService.findWithDetails(id));
    model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
    model.addAttribute("veterinarians", List.of(clinic.getOwner()));
    model.addAttribute("role", receptionist.getRole().name());
  }

  @GetMapping("/schedule")
  public String showScheduleForm(
      @RequestParam(required = false) Long customerId, Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    model.addAttribute("customerId", customerId);
    model.addAttribute("customers", userService.findActiveCustomers());
    model.addAttribute(
        "pets", customerId != null ? petService.findByOwnerId(customerId) : List.of());
    model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
    model.addAttribute("appointment", new Appointment());
    return "receptionist/schedule_appointment";
  }

  @PostMapping("/schedule")
  public String scheduleAppointment(
      @Valid @ModelAttribute("appointment") Appointment appointment,
      BindingResult result,
      @RequestParam("customerId") Long customerId,
      @RequestParam("serviceId") Long serviceId,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
      model.addAttribute("customerId", customerId);
      model.addAttribute("customers", userService.findActiveCustomers());
      model.addAttribute("pets", petService.findByOwnerId(customerId));
      model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
      return "receptionist/schedule_appointment";
    }
    try {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      appointmentService.createReceptionistAppointment(
          appointment, customerId, serviceId, receptionist.getId());
      return REDIRECT_RECEPTIONIST_DASHBOARD;
    } catch (Exception e) {
      AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
      VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
      model.addAttribute("customerId", customerId);
      model.addAttribute("customers", userService.findActiveCustomers());
      model.addAttribute("pets", petService.findByOwnerId(customerId));
      model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
      model.addAttribute("error", e.getMessage());
      return "receptionist/schedule_appointment";
    }
  }
}
