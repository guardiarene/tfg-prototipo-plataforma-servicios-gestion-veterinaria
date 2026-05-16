package tfg.psygcv.clinic.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMedicalServiceCommand {

  private final String name;
  private final String description;
  private final Long clinicId;
}
