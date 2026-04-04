package tfg.psygcv.service.user;

import lombok.Builder;
import lombok.Getter;
import tfg.psygcv.entity.user.Role;

@Builder
@Getter
public class UpdateAdminUserCommand {

  private final String firstName;
  private final String lastName;
  private final String email;
  private final String password;
  private final String phone;
  private final Role role;
  private final Boolean active;
}
