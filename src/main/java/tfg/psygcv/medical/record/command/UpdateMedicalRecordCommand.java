package tfg.psygcv.medical.record.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateMedicalRecordCommand {

  private final String generalObservations;
}
