package tfg.psygcv.dto.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.user.Role;

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
