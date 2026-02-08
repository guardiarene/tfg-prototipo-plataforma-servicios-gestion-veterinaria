package tfg.psygcv.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/customer")
@Controller
public class CustomerController extends BaseController {

  private final VeterinaryClinicServiceInterface veterinaryClinicService;

  @GetMapping("/dashboard")
  public String showClientDashboard(Model model) {
    model.addAttribute("veterinaryClinics", veterinaryClinicService.findAll());
    return "client/dashboard";
  }
}
