package tfg.psygcv.controller.dashboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.dto.clinic.response.VeterinaryClinicSummaryResponse;
import tfg.psygcv.mapper.clinic.VeterinaryClinicMapper;
import tfg.psygcv.service.clinic.VeterinaryClinicService;

@RequiredArgsConstructor
@RequestMapping("/customer")
@Controller
public class CustomerController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/dashboard")
  public String showClientDashboard(Model model) {
    List<VeterinaryClinicSummaryResponse> clinics =
        veterinaryClinicService.findAll().stream().map(VeterinaryClinicMapper::toSummary).toList();
    model.addAttribute("veterinaryClinics", clinics);
    return "customer/dashboard";
  }
}
