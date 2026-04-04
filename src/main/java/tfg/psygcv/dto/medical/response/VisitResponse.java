package tfg.psygcv.dto.medical.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.medical.VisitType;

@Getter
@Setter
@NoArgsConstructor
public class VisitResponse {

  private Long id;
  private Long medicalRecordId;
  private LocalDate date;
  private VisitType visitType;
  private String reasonForVisit;
  private String observations;
  private String veterinarianFullName;
  private ClinicalExamResponse clinicalExam;
  private AnamnesisResponse anamnesis;
  private List<DiagnosticResponse> diagnostics = new ArrayList<>();
  private List<TreatmentResponse> treatments = new ArrayList<>();
  private List<VaccineResponse> vaccines = new ArrayList<>();
}
