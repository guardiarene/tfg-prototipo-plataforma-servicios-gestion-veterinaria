package tfg.psygcv.dto.clinic.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterClinicRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
  private String userFirstName;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(max = 50, message = "El apellido no puede superar 50 caracteres")
  private String userLastName;

  @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
  private String userPhone;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Size(max = 254, message = "El email no puede superar 254 caracteres")
  private String userEmail;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
  private String userPassword;

  @NotBlank(message = "El nombre de la clínica es obligatorio")
  @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
  private String clinicName;

  @NotBlank(message = "La dirección es obligatoria")
  @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
  private String clinicAddress;

  @NotBlank(message = "El teléfono de la clínica es obligatorio")
  @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
  private String clinicPhone;

  @NotBlank(message = "El email de la clínica es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Size(max = 254, message = "El email no puede superar 254 caracteres")
  private String clinicEmail;

  private String confirmPassword;
}
