package tfg.psygcv.medical.visit.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@NoArgsConstructor
public class TreatmentRequest {

  private Long id;

  @NotBlank(message = "El producto es obligatorio")
  private String product;

  @NotBlank(message = "La vía de administración es obligatoria")
  private String route;

  @NotBlank(message = "La frecuencia es obligatoria")
  private String frequency;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate endDate;
}
