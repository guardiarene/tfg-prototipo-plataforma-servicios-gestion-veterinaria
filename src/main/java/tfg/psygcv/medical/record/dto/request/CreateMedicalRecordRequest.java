package tfg.psygcv.medical.record.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMedicalRecordRequest {

  @NotNull(message = "La mascota es obligatoria")
  private Long petId;

  private String generalObservations;
}
