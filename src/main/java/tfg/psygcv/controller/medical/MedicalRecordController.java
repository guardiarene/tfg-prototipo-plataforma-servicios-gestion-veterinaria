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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tfg.psygcv.controller.BaseController;
import tfg.psygcv.entity.medical.Anamnesis;
import tfg.psygcv.entity.medical.ClinicalExam;
import tfg.psygcv.entity.medical.MedicalRecord;
import tfg.psygcv.entity.medical.Visit;
import tfg.psygcv.entity.medical.VisitType;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.medical.MedicalRecordService;
import tfg.psygcv.service.pet.PetService;
import tfg.psygcv.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/medical-records")
@Controller
public class MedicalRecordController extends BaseController {

  private final MedicalRecordService medicalRecordService;
  private final PetService petService;
  private final UserService userService;

  @GetMapping("/{id}")
  public String showMedicalRecordDetails(
      @PathVariable Long id, Model model, Authentication authentication) {
    User user = getCurrentUser(authentication, userService);
    MedicalRecord medicalRecord = medicalRecordService.findCompleteById(id);
    model.addAttribute("medicalRecord", medicalRecord);
    model.addAttribute("role", user.getRole().name());
    return "medical_records/details";
  }

  @GetMapping("/new")
  public String showNewMedicalRecordForm(Model model, Authentication authentication) {
    User veterinarian = getCurrentUser(authentication, userService);
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
      Model model,
      RedirectAttributes ra) {
    User veterinarian = getCurrentUser(authentication, userService);
    if (result.hasErrors()) {
      model.addAttribute("pets", petService.findPetsWithAppointmentsInClinics(veterinarian));
      return "medical_records/new";
    }
    try {
      medicalRecordService.save(medicalRecord, veterinarian);
      ra.addFlashAttribute("success", "Historia clínica creada correctamente.");
      return REDIRECT_VETERINARIAN_DASHBOARD;
    } catch (Exception e) {
      model.addAttribute("pets", petService.findPetsWithAppointmentsInClinics(veterinarian));
      model.addAttribute("error", e.getMessage());
      return "medical_records/new";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
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
      Model model,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      MedicalRecord completeRecord = medicalRecordService.findCompleteForEditing(id);
      medicalRecord.setPet(completeRecord.getPet());
      model.addAttribute("pets", List.of(medicalRecord.getPet()));
      return "medical_records/edit";
    }
    try {
      User veterinarian = getCurrentUser(authentication, userService);
      medicalRecordService.update(id, medicalRecord, veterinarian);
      ra.addFlashAttribute("success", "Historia clínica actualizada correctamente.");
      return "redirect:/medical-records/" + id;
    } catch (Exception e) {
      model.addAttribute("pets", List.of(medicalRecord.getPet()));
      model.addAttribute("error", e.getMessage());
      return "medical_records/edit";
    }
  }

  private MedicalRecord initializeNewMedicalRecord() {
    MedicalRecord medicalRecord = new MedicalRecord();
    Visit firstVisit = new Visit();
    firstVisit.setDate(LocalDate.now());
    firstVisit.setVisitType(VisitType.CONSULTATION);
    firstVisit.setReasonForVisit("");
    firstVisit.setDiagnostics(new ArrayList<>());
    firstVisit.setTreatments(new ArrayList<>());
    firstVisit.setVaccines(new ArrayList<>());
    firstVisit.setAnamnesis(new Anamnesis());
    firstVisit.setClinicalExam(new ClinicalExam());
    medicalRecord.getVisits().add(firstVisit);
    return medicalRecord;
  }
}
