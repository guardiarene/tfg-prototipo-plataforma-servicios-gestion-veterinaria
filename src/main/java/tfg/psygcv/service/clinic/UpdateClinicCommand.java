package tfg.psygcv.service.clinic;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateClinicCommand {

  private final String name;
  private final String address;
  private final String phone;
  private final String email;
}
