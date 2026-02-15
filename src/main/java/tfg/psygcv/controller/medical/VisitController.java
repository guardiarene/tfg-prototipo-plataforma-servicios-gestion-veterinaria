package tfg.psygcv.controller.medical;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
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
import tfg.psygcv.model.medical.Anamnesis;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.medical.VisitType;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.interfaces.VisitServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/visits")
@Controller
public class VisitController extends BaseController {

  private final VisitServiceInterface visitService;
  private final MedicalRecordServiceInterface medicalRecordService;

  @GetMapping("/{id}")
  public String showVisitDetails(@PathVariable Long id, Model model, Authentication authentication) {
    Visit visit = visitService.findCompleteById(id);
    model.addAttribute("visit", visit);
    return "visits/details";
  }

  @GetMapping("/new")
  public String showNewVisitForm(
      @RequestParam Long medicalRecordId, Model model, Authentication authentication) {
    MedicalRecord medicalRecord = medicalRecordService.findCompleteById(medicalRecordId);
    Visit visit = initializeNewVisit();

    model.addAttribute("visit", visit);
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("visitTypes", VisitType.values());

    return "visits/new";
  }

  @PostMapping("/new")
  public String saveVisit(
      @RequestParam Long medicalRecordId,
      @Valid @ModelAttribute("visit") Visit visit,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      MedicalRecord medicalRecord = medicalRecordService.findCompleteById(medicalRecordId);
      model.addAttribute("medicalRecord", medicalRecord);
      model.addAttribute("visitTypes", VisitType.values());
      return "visits/new";
    }

    User veterinarian = getCurrentUser(authentication);
    Visit savedVisit = visitService.createVisit(medicalRecordId, visit, veterinarian);

    return "redirect:/visits/" + savedVisit.getId();
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
    Visit visit = visitService.findCompleteById(id);
    model.addAttribute("visit", visit);
    model.addAttribute("visitTypes", VisitType.values());
    return "visits/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateVisit(
      @PathVariable Long id,
      @Valid @ModelAttribute("visit") Visit visit,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("visitTypes", VisitType.values());
      return "visits/edit";
    }

    User veterinarian = getCurrentUser(authentication);
    Visit updatedVisit = visitService.updateVisit(id, visit, veterinarian);

    return "redirect:/visits/" + updatedVisit.getId();
  }

  @PostMapping("/{id}/delete")
  public String deleteVisit(
      @PathVariable Long id, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication);
    Visit visit = visitService.findCompleteById(id);
    Long medicalRecordId = visit.getMedicalRecord().getId();

    visitService.deleteVisit(id, veterinarian);

    return "redirect:/medical-records/" + medicalRecordId;
  }

  private Visit initializeNewVisit() {
    Visit visit = new Visit();
    visit.setDate(LocalDate.now());
    visit.setVisitType(VisitType.CONSULTATION);
    visit.setDiagnostics(new ArrayList<>());
    visit.setTreatments(new ArrayList<>());
    visit.setVaccines(new ArrayList<>());

    // Initialize anamnesis
    Anamnesis anamnesis = new Anamnesis();
    visit.setAnamnesis(anamnesis);

    return visit;
  }
}
