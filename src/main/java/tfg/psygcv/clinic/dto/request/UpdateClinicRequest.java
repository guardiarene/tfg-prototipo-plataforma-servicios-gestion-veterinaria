package tfg.psygcv.clinic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateClinicRequest {

  @NotBlank(message = "El nombre de la clínica es obligatorio")
  @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
  private String name;

  @NotBlank(message = "La dirección es obligatoria")
  @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
  private String address;

  @NotBlank(message = "El teléfono es obligatorio")
  @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
  private String phone;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Size(max = 254, message = "El email no puede superar 254 caracteres")
  private String email;
}
