package tfg.psygcv.controller.medical;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_VETERINARIAN_DASHBOARD;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
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
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.medical.Anamnesis;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.medical.VisitType;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.interfaces.PetServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/medical-records")
@Controller
public class MedicalRecordController extends BaseController {

  private final MedicalRecordServiceInterface medicalRecordService;

  private final PetServiceInterface petService;

  @GetMapping("/{id}")
  public String showMedicalRecordDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    User user = getCurrentUser(authentication);
    MedicalRecord medicalRecord = medicalRecordService.findCompleteById(id);
    model.addAttribute("medicalRecord", medicalRecord);
    return "medical_records/details";
  }

  @GetMapping("/new")
  public String showNewMedicalRecordForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication);
    MedicalRecord medicalRecord = initializeNewMedicalRecord();
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("pets", petService.findPetsWithAppointmentsInClinics(veterinarian));
    return "medical_records/new";
  }

  @PostMapping("/new")
  public String saveMedicalRecord(
      @Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      User veterinarian = getCurrentUser(authentication);
      model.addAttribute("pets", petService.findPetsWithAppointmentsInClinics(veterinarian));
      return "medical_records/new";
    }
    User veterinarian = getCurrentUser(authentication);
    medicalRecordService.save(medicalRecord, veterinarian);
    return REDIRECT_VETERINARIAN_DASHBOARD;
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
    MedicalRecord medicalRecord = medicalRecordService.findCompleteForEditing(id);
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("pets", List.of(medicalRecord.getPet()));
    return "medical_records/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateMedicalRecord(
      @PathVariable Long id,
      @Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
      BindingResult result,
      Authentication authentication,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("pets", List.of(medicalRecord.getPet()));
      return "medical_records/edit";
    }
    User veterinarian = getCurrentUser(authentication);
    medicalRecordService.update(id, medicalRecord, veterinarian);
    return "redirect:/medical-records/" + id;
  }

  /**
   * Initializes a new MedicalRecord with a first Visit for backward compatibility with existing
   * forms
   */
  private MedicalRecord initializeNewMedicalRecord() {
    MedicalRecord medicalRecord = new MedicalRecord();

    // Create first visit (backward compatibility)
    Visit firstVisit = new Visit();
    firstVisit.setDate(LocalDate.now());
    firstVisit.setVisitType(VisitType.CONSULTATION);
    firstVisit.setReasonForVisit("");
    firstVisit.setDiagnostics(new ArrayList<>());
    firstVisit.setTreatments(new ArrayList<>());
    firstVisit.setVaccines(new ArrayList<>());

    // Initialize anamnesis
    Anamnesis anamnesis = new Anamnesis();
    firstVisit.setAnamnesis(anamnesis);

    // Add first visit to medical record
    medicalRecord.getVisits().add(firstVisit);

    return medicalRecord;
  }
}
