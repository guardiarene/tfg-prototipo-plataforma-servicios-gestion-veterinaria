package tfg.psygcv.clinic.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateMedicalServiceCommand {

  private final String name;
  private final String description;
}
