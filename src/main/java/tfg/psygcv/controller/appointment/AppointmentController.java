package tfg.psygcv.controller.appointment;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_MY_APPOINTMENTS;
import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_RECEPTIONIST_DASHBOARD;

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
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
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
    User currentUser = getCurrentUser(authentication);
    model.addAttribute("appointments", appointmentService.findByCustomerId(currentUser.getId()));
    return "appointments/list";
  }

  @GetMapping("/new")
  public String showNewAppointmentForm(
      @RequestParam("clinicId") Long clinicId, Model model, Authentication authentication) {
    User currentUser = getCurrentUser(authentication);
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
      Authentication authentication) {
    User currentUser = getCurrentUser(authentication);
    appointmentService.createClientAppointment(date, petId, serviceId, clinicId, currentUser);
    return REDIRECT_MY_APPOINTMENTS;
  }

  @GetMapping("/{id}")
  public String showAppointmentDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    User user = getCurrentUser(authentication);
    Appointment appointment = appointmentService.findWithDetails(id);
    model.addAttribute("appointment", appointment);
    model.addAttribute("veterinarianName", appointmentService.findVeterinarianName(appointment));
    return "appointments/details";
  }

  @PostMapping("/{id}/cancel")
  public String cancelAppointment(@PathVariable Long id) {
    appointmentService.cancel(id);
    return REDIRECT_MY_APPOINTMENTS;
  }

  @PostMapping("/{id}/update-status")
  public String updateAppointmentStatus(
      @PathVariable Long id, @RequestParam("status") AppointmentStatus status) {
    appointmentService.updateStatus(id, status);
    return REDIRECT_RECEPTIONIST_DASHBOARD;
  }

  @GetMapping("/{id}/reschedule")
  public String showRescheduleForm(
      @PathVariable Long id, Model model, Authentication authentication) {
    User receptionist = getCurrentUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    model.addAttribute("appointment", appointmentService.findById(id));
    model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
    model.addAttribute("veterinarians", List.of(clinic.getVeterinarian()));
    return "receptionist/reschedule_appointment";
  }

  @PostMapping("/{id}/reschedule")
  public String rescheduleAppointment(
      @PathVariable Long id,
      @Valid @ModelAttribute("appointment") Appointment appointment,
      BindingResult result) {
    if (result.hasErrors()) {
      return "receptionist/reschedule_appointment";
    }
    appointmentService.reschedule(id, appointment);
    return REDIRECT_RECEPTIONIST_DASHBOARD;
  }

  @GetMapping("/schedule")
  public String showScheduleForm(
      @RequestParam(required = false) Long clientId, Model model, Authentication authentication) {
    User receptionist = getCurrentUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    model.addAttribute("clientId", clientId);
    model.addAttribute("clients", userService.findActiveCustomers());
    model.addAttribute("pets", clientId != null ? petService.findByOwnerId(clientId) : List.of());
    model.addAttribute("services", medicalServiceService.findByClinicId(clinic.getId()));
    model.addAttribute("appointment", new Appointment());
    return "receptionist/schedule_appointment";
  }

  @PostMapping("/schedule")
  public String scheduleAppointment(
      @Valid @ModelAttribute("appointment") Appointment appointment,
      BindingResult result,
      @RequestParam("serviceId") Long serviceId,
      Authentication authentication) {
    if (result.hasErrors()) {
      return "receptionist/schedule_appointment";
    }
    User receptionist = getCurrentUser(authentication);
    appointmentService.createReceptionistAppointment(appointment, serviceId, receptionist.getId());
    return REDIRECT_RECEPTIONIST_DASHBOARD;
  }
}
