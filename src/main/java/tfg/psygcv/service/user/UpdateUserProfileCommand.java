package tfg.psygcv.service.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateUserProfileCommand {

  private final String firstName;
  private final String lastName;
  private final String email;
  private final String phone;
}
