package tfg.psygcv.medical.record.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecordSummaryResponse {

  private Long id;
  private String petName;
  private String petSpeciesDescription;
  private String ownerFullName;
}
