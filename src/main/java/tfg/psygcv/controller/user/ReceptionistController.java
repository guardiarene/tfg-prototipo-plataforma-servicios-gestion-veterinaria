package tfg.psygcv.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.AppointmentServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/receptionist")
@Controller
public class ReceptionistController extends BaseController {

    private final AppointmentServiceInterface appointmentService;

    private final VeterinaryClinicServiceInterface veterinaryClinicService;

    @GetMapping("/dashboard")
    public String showReceptionistDashboard(Model model, Authentication authentication) {
        User receptionist = getCurrentUser(authentication);
        VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionist.getId());
        model.addAttribute("appointmentStatuses", AppointmentStatus.values());
        model.addAttribute("appointments", appointmentService.findByClinicId(clinic.getId()));
        return "receptionist/dashboard";
    }

}
