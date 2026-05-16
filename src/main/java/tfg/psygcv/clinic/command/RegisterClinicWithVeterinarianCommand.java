package tfg.psygcv.clinic.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterClinicWithVeterinarianCommand {

  private final String userFirstName;
  private final String userLastName;
  private final String userEmail;
  private final String userPassword;
  private final String userPhone;
  private final String clinicName;
  private final String clinicAddress;
  private final String clinicPhone;
  private final String clinicEmail;
}
