package tfg.psygcv.appointment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.appointment.dto.response.AppointmentSummaryResponse;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.appointment.mapper.AppointmentMapper;
import tfg.psygcv.appointment.service.AppointmentService;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/receptionist")
@Controller
public class ReceptionistDashboardController extends BaseController {

  private final AppointmentService appointmentService;
  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/dashboard")
  public String showReceptionistDashboard(Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    Long clinicId = veterinaryClinicService.findClinicIdByReceptionistId(receptionist.getId());
    List<AppointmentSummaryResponse> appointments =
        appointmentService.findByClinicId(clinicId).stream()
            .map(AppointmentMapper::toSummary)
            .toList();
    model.addAttribute("appointmentStatuses", AppointmentStatus.values());
    model.addAttribute("appointments", appointments);
    model.addAttribute("role", receptionist.getRole().name());
    return "receptionist/dashboard";
  }
}
