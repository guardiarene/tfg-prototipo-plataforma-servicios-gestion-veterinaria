package tfg.psygcv.service.medical;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMedicalRecordCommand {

  private final Long petId;
  private final String generalObservations;
}
