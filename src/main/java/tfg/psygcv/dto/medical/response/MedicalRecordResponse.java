package tfg.psygcv.dto.medical.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecordResponse {

  private Long id;
  private String petName;
  private String petSpeciesDescription;
  private String petBreedDescription;
  private String ownerFullName;
  private String generalObservations;
  private AnamnesisResponse currentAnamnesis;
  private List<VaccineResponse> vaccines;
  private List<VisitResponse> visits;
}
