package tfg.psygcv.controller.dashboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.appointment.response.AppointmentSummaryResponse;
import tfg.psygcv.entity.appointment.AppointmentStatus;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.mapper.appointment.AppointmentMapper;
import tfg.psygcv.service.appointment.AppointmentService;
import tfg.psygcv.service.clinic.VeterinaryClinicService;

@RequiredArgsConstructor
@RequestMapping("/receptionist")
@Controller
public class ReceptionistController extends BaseController {

  private final AppointmentService appointmentService;
  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/dashboard")
  public String showReceptionistDashboard(Model model, Authentication authentication) {
    AuthenticatedUser receptionist = getAuthenticatedUser(authentication);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
    List<AppointmentSummaryResponse> appointments =
        appointmentService.findByClinicId(clinic.getId()).stream()
            .map(AppointmentMapper::toSummary)
            .toList();
    model.addAttribute("appointmentStatuses", AppointmentStatus.values());
    model.addAttribute("appointments", appointments);
    model.addAttribute("role", receptionist.getRole().name());
    return "receptionist/dashboard";
  }
}
