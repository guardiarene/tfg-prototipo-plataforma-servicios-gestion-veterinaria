package tfg.psygcv.medical.record.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMedicalRecordCommand {

  private final Long petId;
  private final String generalObservations;
}
