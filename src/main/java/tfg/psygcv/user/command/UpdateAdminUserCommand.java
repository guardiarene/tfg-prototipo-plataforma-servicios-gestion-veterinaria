package tfg.psygcv.user.command;

import lombok.Builder;
import lombok.Getter;
import tfg.psygcv.user.entity.Role;

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
