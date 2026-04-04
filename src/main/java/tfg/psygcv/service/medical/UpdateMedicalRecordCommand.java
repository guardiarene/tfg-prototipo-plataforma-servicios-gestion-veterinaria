package tfg.psygcv.service.medical;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateMedicalRecordCommand {

  private final String generalObservations;
}
