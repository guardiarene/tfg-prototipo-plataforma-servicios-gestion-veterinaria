package tfg.psygcv.clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMedicalServiceRequest {

  @NotBlank(message = "El nombre del servicio es obligatorio")
  @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
  private String name;

  private String description;
}
