package tfg.psygcv.medical.visit.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import tfg.psygcv.medical.visit.entity.VisitType;

@Getter
@Setter
@NoArgsConstructor
public class UpdateVisitRequest {

  @NotNull(message = "La fecha de la visita es obligatoria")
  @PastOrPresent(message = "La fecha no puede ser futura")
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate date;

  @NotBlank(message = "El motivo de consulta es obligatorio")
  private String reasonForVisit;

  @NotNull(message = "El tipo de visita es obligatorio")
  private VisitType visitType;

  private String observations;

  @Valid
  private ClinicalExamRequest clinicalExam;

  @Valid
  private AnamnesisRequest anamnesis;

  @Valid
  private List<DiagnosticRequest> diagnostics = new ArrayList<>();

  @Valid
  private List<TreatmentRequest> treatments = new ArrayList<>();

  @Valid
  private List<VaccineRequest> vaccines = new ArrayList<>();
}
