package tfg.psygcv.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.user.entity.Role;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Role role;
  private Boolean active;
}
