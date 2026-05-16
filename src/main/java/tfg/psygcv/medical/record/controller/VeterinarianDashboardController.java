package tfg.psygcv.medical.record.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.medical.record.dto.response.MedicalRecordSummaryResponse;
import tfg.psygcv.medical.record.mapper.MedicalRecordMapper;
import tfg.psygcv.medical.record.service.MedicalRecordService;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/veterinarian")
@Controller
public class VeterinarianDashboardController extends BaseController {

  private final MedicalRecordService medicalRecordService;

  @GetMapping("/dashboard")
  public String showDashboard(Model model, Authentication authentication) {
    Long veterinarianId = getAuthenticatedUser(authentication).getId();
    List<MedicalRecordSummaryResponse> medicalRecords =
        medicalRecordService.findByVeterinarian(veterinarianId).stream()
            .map(MedicalRecordMapper::toSummary)
            .toList();
    model.addAttribute("medicalRecords", medicalRecords);
    return "veterinarian/dashboard";
  }
}
