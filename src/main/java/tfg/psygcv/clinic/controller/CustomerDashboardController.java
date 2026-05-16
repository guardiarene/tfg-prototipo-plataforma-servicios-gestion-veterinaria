package tfg.psygcv.clinic.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.clinic.dto.response.VeterinaryClinicSummaryResponse;
import tfg.psygcv.clinic.mapper.VeterinaryClinicMapper;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.shared.controller.BaseController;

@RequiredArgsConstructor
@RequestMapping("/customer")
@Controller
public class CustomerDashboardController extends BaseController {

  private final VeterinaryClinicService veterinaryClinicService;

  @GetMapping("/dashboard")
  public String showClientDashboard(Model model) {
    List<VeterinaryClinicSummaryResponse> clinics =
        veterinaryClinicService.findAll().stream().map(VeterinaryClinicMapper::toSummary).toList();
    model.addAttribute("veterinaryClinics", clinics);
    return "customer/dashboard";
  }
}
