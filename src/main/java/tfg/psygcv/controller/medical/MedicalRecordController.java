package tfg.psygcv.controller.medical;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.controller.base.BaseController;
import tfg.psygcv.model.medical.Anamnesis;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.interfaces.MedicalRecordServiceInterface;
import tfg.psygcv.service.interfaces.PetServiceInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static tfg.psygcv.config.constant.RouteConstant.REDIRECT_VETERINARIAN_DASHBOARD;

@RequiredArgsConstructor
@RequestMapping("/medical-records")
@Controller
public class MedicalRecordController extends BaseController {

    private final MedicalRecordServiceInterface medicalRecordService;

    private final PetServiceInterface petService;

    @GetMapping("/{id}")
    public String showMedicalRecordDetails(@PathVariable Long id,
                                           Model model,
                                           Authentication authentication) {
        User user = getCurrentUser(authentication);
        model.addAttribute("medicalRecord", medicalRecordService.findCompleteForEditing(id));
        return user.getRole() == Role.VETERINARIAN
                ? "medical_records/details"
                : "pets/medical_record";
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
    public String saveMedicalRecord(@Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
                                    BindingResult result,
                                    Authentication authentication) {
        if (result.hasErrors()) {
            return "medical_records/new";
        }
        User veterinarian = getCurrentUser(authentication);
        medicalRecord.setVeterinarian(veterinarian);
        medicalRecord.setDate(LocalDate.now());
        medicalRecordService.save(medicalRecord, veterinarian);
        return REDIRECT_VETERINARIAN_DASHBOARD;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               Authentication authentication) {
        MedicalRecord medicalRecord = medicalRecordService.findCompleteForEditing(id);
        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("pets", List.of(medicalRecord.getPet()));
        return "medical_records/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateMedicalRecord(@PathVariable Long id,
                                      @Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
                                      BindingResult result,
                                      Authentication authentication) {
        if (result.hasErrors()) {
            return "medical_records/edit";
        }
        User veterinarian = getCurrentUser(authentication);
        medicalRecordService.update(id, medicalRecord, veterinarian);
        return "redirect:/medical-records/" + id;
    }

    private MedicalRecord initializeNewMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setTreatments(new ArrayList<>());
        medicalRecord.setDiagnostics(new ArrayList<>());
        medicalRecord.setAnamnesis(new Anamnesis());
        medicalRecord.getAnamnesis().setVaccines(new ArrayList<>());
        return medicalRecord;
    }

}
