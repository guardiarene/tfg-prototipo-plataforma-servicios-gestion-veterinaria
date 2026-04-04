package tfg.psygcv.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.user.Role;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAdminUserRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
  private String firstName;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(max = 50, message = "El apellido no puede superar 50 caracteres")
  private String lastName;

  @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
  private String phone;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Size(max = 254, message = "El email no puede superar 254 caracteres")
  private String email;

  @NotNull(message = "El rol es obligatorio")
  private Role role;

  private Boolean active;

  private String password;
}
