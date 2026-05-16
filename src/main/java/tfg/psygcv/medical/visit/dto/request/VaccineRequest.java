package tfg.psygcv.medical.visit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@NoArgsConstructor
public class VaccineRequest {

  private Long id;

  @NotNull(message = "La fecha de aplicación es obligatoria")
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate applicationDate;

  @NotBlank(message = "La marca es obligatoria")
  private String brand;

  @NotBlank(message = "La dosis es obligatoria")
  private String dose;

  @NotBlank(message = "El lote es obligatorio")
  private String batch;
}
